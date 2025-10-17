package com.micros.mslocations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.micros.mslocations.model.ProximityAlert;
import com.micros.mslocations.repository.ProximityAlertRepository;

@Service
public class KafkaProducerService {
    
    private static final String TOPIC = "proximity-alerts";
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    
    @Autowired
    private KafkaTemplate<String, ProximityAlert> kafkaTemplate;
    
    @Autowired
    private ProximityAlertRepository proximityAlertRepository;
    
    public void sendProximityAlert(ProximityAlert alert) {
        try {
            // Sauvegarder en base de données
            proximityAlertRepository.save(alert);
            
            String key = alert.getUserId1() + "-" + alert.getUserId2();
            Message<ProximityAlert> message = MessageBuilder
                .withPayload(alert)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build();
            
            kafkaTemplate.send(message);
            logger.info("Alerte de proximité envoyée : User {} et {} à {} km", 
                alert.getUserId1(), alert.getUserId2(), alert.getDistance());
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'alerte Kafka", e);
        }
    }
}