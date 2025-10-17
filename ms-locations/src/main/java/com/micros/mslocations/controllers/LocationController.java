package com.micros.mslocations.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.micros.mslocations.model.Location;
import com.micros.mslocations.model.ProximityAlert;
import com.micros.mslocations.dto.UserLocationDTO;
import com.micros.mslocations.dto.AddLocationRequest;
import com.micros.mslocations.repository.LocationRepository;
import com.micros.mslocations.repository.ProximityAlertRepository;
import com.micros.mslocations.service.DistanceService;
import com.micros.mslocations.service.KafkaProducerService;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping(path="/locations")
public class LocationController {
    
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    private static final Double PROXIMITY_THRESHOLD_KM = 1.0; // 1 km
    
    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private ProximityAlertRepository proximityAlertRepository;
    
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping(path="/add")
    public @ResponseBody String addNewLocation(@RequestBody AddLocationRequest request) {
        
        Location location = new Location();
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setTimestamp(LocalDateTime.now());
        if (request.getUserId() != null) {
            location.setUserId(request.getUserId());
        }
        locationRepository.save(location);
        
        logger.info("Localisation sauvegardée - User: {}, Lat: {}, Lon: {}", 
            request.getUserId(), request.getLatitude(), request.getLongitude());
        
        // Vérifier la proximité avec d'autres utilisateurs
        if (request.getUserId() != null) {
            checkProximity(location);
        }
        
        return "Location saved successfully";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @GetMapping(path="/user/{userId}")
    public @ResponseBody UserLocationDTO getLocationsByUser(@PathVariable Long userId) {
        List<Location> locations = locationRepository.findByUserId(userId);
        
        // Récupérer toutes les alertes concernant cet utilisateur
        List<ProximityAlert> alertsAsUser1 = proximityAlertRepository.findByUserId1(userId);
        List<ProximityAlert> alertsAsUser2 = proximityAlertRepository.findByUserId2(userId);
        
        // Combiner les deux listes
        List<ProximityAlert> allAlerts = new ArrayList<>();
        allAlerts.addAll(alertsAsUser1);
        allAlerts.addAll(alertsAsUser2);
        
        logger.info("Récupération des infos User {} - {} locations, {} alertes", 
            userId, locations.size(), allAlerts.size());
        
        return new UserLocationDTO(userId, locations, allAlerts);
    }

    @GetMapping(path="/{id}")
    public @ResponseBody Optional<Location> getLocationById(@PathVariable Long id) {
        return locationRepository.findById(id);
    }
    
    /**
     * Vérifie si l'utilisateur est trop proche d'un autre
     */
    private void checkProximity(Location newLocation) {
        Iterable<Location> allLocations = locationRepository.findAll();
        Set<String> sentAlerts = new HashSet<>();
        
        for (Location otherLocation : allLocations) {
            // Ne pas comparer avec soi-même
            if (newLocation.getId().equals(otherLocation.getId())) {
                continue;
            }
            
            // Vérifier que l'autre location a un userId
            if (otherLocation.getUserId() == null || 
                newLocation.getUserId() == null) {
                continue;
            }
            
            // Calculer la distance
            double distance = DistanceService.calculateDistance(
                newLocation.getLatitude(), newLocation.getLongitude(),
                otherLocation.getLatitude(), otherLocation.getLongitude()
            );
            
            logger.debug("Distance entre User {} et User {}: {} km",
                newLocation.getUserId(), otherLocation.getUserId(), distance);
            
            // Si trop proche, envoyer une alerte
            if (distance <= PROXIMITY_THRESHOLD_KM) {
                // Éviter les doublons (A-B et B-A)
                String alertKey1 = Math.min(newLocation.getUserId(), otherLocation.getUserId()) + 
                                   "-" + Math.max(newLocation.getUserId(), otherLocation.getUserId());
                
                if (!sentAlerts.contains(alertKey1)) {
                    ProximityAlert alert = new ProximityAlert(
                        newLocation.getUserId(),
                        otherLocation.getUserId(),
                        distance,
                        newLocation.getLatitude(),
                        newLocation.getLongitude(),
                        otherLocation.getLatitude(),
                        otherLocation.getLongitude()
                    );
                    
                    kafkaProducerService.sendProximityAlert(alert);
                    sentAlerts.add(alertKey1);
                    
                    logger.warn("⚠️ ALERTE PROXIMITÉ: User {} et User {} sont à {} km l'un de l'autre!",
                        newLocation.getUserId(), otherLocation.getUserId(), distance);
                }
            }
        }
    }
}