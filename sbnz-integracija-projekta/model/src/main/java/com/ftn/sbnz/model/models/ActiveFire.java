package com.ftn.sbnz.model.models;

import com.ftn.sbnz.model.models.enums.*;

import java.io.Serializable;

public class ActiveFire implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long fireIncidentId;
    private FireClass fireClass;
    private WindSpeed windSpeed;
    private Risk spreadRisk;
    private WindDirection spreadDirection;
    private FireSize fireSize;
    private ExtinguisherType extinguisher;
    private int numOfFirePoints;
    private WarningType warning;
    private String typeOfExtinguisher;
    private FireDevelopmentLocation fireDevelopmentLocation;

    public ActiveFire() {
    }

    public ActiveFire(FireIncident fire) {
        this.fireIncidentId = fire.getId();
        this.spreadDirection = fire.getWindDirection();
    }

    public Long getFireIncidentId() {
        return fireIncidentId;
    }

    public void setFireIncidentId(Long fireIncidentId) {
        this.fireIncidentId = fireIncidentId;
    }

    public FireClass getFireClass() {
        return fireClass;
    }

    public void setFireClass(FireClass fireClass) {
        this.fireClass = fireClass;
    }

    public WindSpeed getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(WindSpeed windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Risk getSpreadRisk() {
        return spreadRisk;
    }

    public void setSpreadRisk(Risk spreadRisk) {
        this.spreadRisk = spreadRisk;
    }

    public WindDirection getSpreadDirection() {
        return spreadDirection;
    }

    public void setSpreadDirection(WindDirection spreadDirection) {
        this.spreadDirection = spreadDirection;
    }

    public FireSize getFireSize() {
        return fireSize;
    }

    public void setFireSize(FireSize fireSize) {
        this.fireSize = fireSize;
    }

    public ExtinguisherType getExtinguisher() {
        return extinguisher;
    }

    public void setExtinguisher(ExtinguisherType extinguisher) {
        this.extinguisher = extinguisher;
    }

    public int getNumOfFirePoints() {
        return numOfFirePoints;
    }

    public void setNumOfFirePoints(int numOfFirePoints) {
        this.numOfFirePoints = numOfFirePoints;
    }

    public WarningType getWarning() {
        return warning;
    }

    public void setWarning(WarningType warning) {
        this.warning = warning;
    }

    public String getTypeOfExtinguisher() {
        return typeOfExtinguisher;
    }

    public void setTypeOfExtinguisher(String typeOfExtinguisher) {
        this.typeOfExtinguisher = typeOfExtinguisher;
    }

    public FireDevelopmentLocation getFireDevelopmentLocation() {
        return fireDevelopmentLocation;
    }

    public void setFireDevelopmentLocation(FireDevelopmentLocation fireDevelopmentLocation) {
        this.fireDevelopmentLocation = fireDevelopmentLocation;
    }
    @Override
    public String toString() {
        return "ActiveFire{" +
                "fireIncidentId=" + fireIncidentId +
                ", fireClass=" + fireClass +
                ", windSpeed=" + windSpeed +
                ", spreadRisk=" + spreadRisk +
                ", spreadDirection=" + spreadDirection +
                ", fireSize=" + fireSize +
                ", extinguisher=" + extinguisher +
                ", numOfFirePoints=" + numOfFirePoints +
                ", warning=" + warning +
                ", typeOfExtinguisher='" + typeOfExtinguisher + '\'' +
                ", fireDevelopmentLocation=" + fireDevelopmentLocation +
                '}';
    }
}
