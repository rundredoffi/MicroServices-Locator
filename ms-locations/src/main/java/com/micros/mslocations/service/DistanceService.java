package com.micros.mslocations.service;

public class DistanceService {
    
    private static final double EARTH_RADIUS_KM = 6371;
    
    /**
     * Calcule la distance entre deux points GPS en kilomètres
     * Utilise la formule de Haversine
     */
    public static double calculateDistance(Double lat1, Double lon1, 
                                          Double lat2, Double lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return Double.MAX_VALUE;
        }
        
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}