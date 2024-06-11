package com.ftn.sbnz.model.models;

import java.io.Serializable;

public class FirefighterObservation implements Serializable {

    private Double flameIntensity;
    private Double fireArea;
    private Double smokeVolume;
    private Long fireId;
    private int numOfFirePoints;

    public FirefighterObservation(Double flameIntensity, Double fireArea, Double smokeVolume, Long fireId, int numOfFirePoints) {
        this.flameIntensity = flameIntensity;
        this.fireArea = fireArea;
        this.smokeVolume = smokeVolume;
        this.fireId = fireId;
        this.numOfFirePoints = numOfFirePoints;
    }

    public FirefighterObservation() {
    }

    public Double getFlameIntensity() {
        return flameIntensity;
    }

    public void setFlameIntensity(Double flameIntensity) {
        this.flameIntensity = flameIntensity;
    }

    public Double getFireArea() {
        return fireArea;
    }

    public void setFireArea(Double fireArea) {
        this.fireArea = fireArea;
    }

    public Double getSmokeVolume() {
        return smokeVolume;
    }

    public void setSmokeVolume(Double smokeVolume) {
        this.smokeVolume = smokeVolume;
    }

    public Long getFireId() {
        return fireId;
    }

    public void setFireId(Long fireId) {
        this.fireId = fireId;
    }

    public int getNumOfFirePoints() {
        return numOfFirePoints;
    }

    public void setNumOfFirePoints(int numOfFirePoints) {
        this.numOfFirePoints = numOfFirePoints;
    }
}
