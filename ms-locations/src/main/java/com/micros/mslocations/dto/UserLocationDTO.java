package com.micros.mslocations.dto;

import com.micros.mslocations.model.Location;
import com.micros.mslocations.model.ProximityAlert;
import java.util.List;

public class UserLocationDTO {
    private Long userId;
    private List<Location> locations;
    private List<ProximityAlert> proximityAlerts;

    public UserLocationDTO(Long userId, List<Location> locations, List<ProximityAlert> proximityAlerts) {
        this.userId = userId;
        this.locations = locations;
        this.proximityAlerts = proximityAlerts;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<ProximityAlert> getProximityAlerts() {
        return proximityAlerts;
    }

    public void setProximityAlerts(List<ProximityAlert> proximityAlerts) {
        this.proximityAlerts = proximityAlerts;
    }
}
