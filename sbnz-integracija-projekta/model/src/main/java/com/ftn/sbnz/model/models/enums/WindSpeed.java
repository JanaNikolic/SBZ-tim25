package com.ftn.sbnz.model.models.enums;

public enum WindSpeed {

    LOW("Wind speed less or equal to 1,2km/h"), MODERATE("Wind speed form 1,2km/h to 9,3km/h"), HIGH("Wind speed more than 9,3km/h");

    private final String description;
    WindSpeed(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
