package com.micros.mslocations.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "proximity_alert")
public class ProximityAlert {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long userId1;
    private Long userId2;
    private Double distance;
    private LocalDateTime timestamp;
    private Double latitude1;
    private Double longitude1;
    private Double latitude2;
    private Double longitude2;

    public ProximityAlert() {
    }

    public ProximityAlert(Long userId1, Long userId2, Double distance, 
                         Double lat1, Double lon1, Double lat2, Double lon2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.distance = distance;
        this.timestamp = LocalDateTime.now();
        this.latitude1 = lat1;
        this.longitude1 = lon1;
        this.latitude2 = lat2;
        this.longitude2 = lon2;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId1() { return userId1; }
    public void setUserId1(Long userId1) { this.userId1 = userId1; }

    public Long getUserId2() { return userId2; }
    public void setUserId2(Long userId2) { this.userId2 = userId2; }

    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Double getLatitude1() { return latitude1; }
    public void setLatitude1(Double latitude1) { this.latitude1 = latitude1; }

    public Double getLongitude1() { return longitude1; }
    public void setLongitude1(Double longitude1) { this.longitude1 = longitude1; }

    public Double getLatitude2() { return latitude2; }
    public void setLatitude2(Double latitude2) { this.latitude2 = latitude2; }

    public Double getLongitude2() { return longitude2; }
    public void setLongitude2(Double longitude2) { this.longitude2 = longitude2; }
}