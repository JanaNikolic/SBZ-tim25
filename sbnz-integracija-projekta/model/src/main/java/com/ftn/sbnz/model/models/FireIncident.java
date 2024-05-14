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

    public FireIncident() {
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

}
