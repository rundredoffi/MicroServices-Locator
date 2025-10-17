package com.micros.mslocations.dto;

public class AddLocationRequest {
    private Double latitude;
    private Double longitude;
    private Long userId;

    public AddLocationRequest() {
    }

    public AddLocationRequest(Double latitude, Double longitude, Long userId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
