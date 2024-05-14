package com.ftn.sbnz.model.models;

import com.ftn.sbnz.model.models.enums.*;

import java.io.Serializable;

public class FireIncident implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private BurningMatter matter;
    private StructureType structure;
    private FlamesType flames;
    private Double volume;
    private SmokeType smoke;
    private Double windSpeed;
    private WindDirection windDirection;
    private Double proximityToResidentialArea;
    private RoomPlacement roomPlacement;
    private Double voltage;
    private boolean peopleInVicinity;
    private Double proximityOfPeopleToFire;

    public FireIncident() {
    }

    public FireIncident(Long id, BurningMatter matter, StructureType structure, FlamesType flames, Double volume, SmokeType smoke, Double windSpeed, WindDirection windDirection, Double proximityToResidentialArea, RoomPlacement roomPlacement, Double voltage, boolean peopleInVicinity, Double proximityOfPeopleToFire) {
        this.id = id;
        this.matter = matter;
        this.structure = structure;
        this.flames = flames;
        this.volume = volume;
        this.smoke = smoke;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.proximityToResidentialArea = proximityToResidentialArea;
        this.roomPlacement = roomPlacement;
        this.voltage = voltage;
        this.peopleInVicinity = peopleInVicinity;
        this.proximityOfPeopleToFire = proximityOfPeopleToFire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BurningMatter getMatter() {
        return matter;
    }

    public void setMatter(BurningMatter matter) {
        this.matter = matter;
    }

    public StructureType getStructure() {
        return structure;
    }

    public void setStructure(StructureType structure) {
        this.structure = structure;
    }

    public FlamesType getFlames() {
        return flames;
    }

    public void setFlames(FlamesType flames) {
        this.flames = flames;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public SmokeType getSmoke() {
        return smoke;
    }

    public void setSmoke(SmokeType smoke) {
        this.smoke = smoke;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public WindDirection getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(WindDirection windDirection) {
        this.windDirection = windDirection;
    }

    public Double getProximityToResidentialArea() {
        return proximityToResidentialArea;
    }

    public void setProximityToResidentialArea(Double proximityToResidentialArea) {
        this.proximityToResidentialArea = proximityToResidentialArea;
    }

    public RoomPlacement getRoomPlacement() {
        return roomPlacement;
    }

    public void setRoomPlacement(RoomPlacement roomPlacement) {
        this.roomPlacement = roomPlacement;
    }

    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    public boolean isPeopleInVicinity() {
        return peopleInVicinity;
    }

    public void setPeopleInVicinity(boolean peopleInVicinity) {
        this.peopleInVicinity = peopleInVicinity;
    }

    public Double getProximityOfPeopleToFire() {
        return proximityOfPeopleToFire;
    }

    public void setProximityOfPeopleToFire(Double proximityOfPeopleToFire) {
        this.proximityOfPeopleToFire = proximityOfPeopleToFire;
    }
}
