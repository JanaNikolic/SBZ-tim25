package com.ftn.sbnz.service.dto;

import com.ftn.sbnz.model.models.FireIncident;
import com.ftn.sbnz.model.models.enums.*;

public class FireIncidentDTO {
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
    private FireCompanyDataDTO fireCompany;
    private boolean finished;

    public FireIncidentDTO(FireIncident fireIncident) {
        this.id = fireIncident.getId();
        this.matter = fireIncident.getMatter();
        this.structure = fireIncident.getStructure();
        this.flames = fireIncident.getFlames();
        this.volume = fireIncident.getVolume();
        this.smoke = fireIncident.getSmoke();
        this.windSpeed = fireIncident.getWindSpeed();
        this.windDirection = fireIncident.getWindDirection();
        this.proximityToResidentialArea = fireIncident.getProximityToResidentialArea();
        this.roomPlacement = fireIncident.getRoomPlacement();
        this.voltage = fireIncident.getVoltage();
        this.peopleInVicinity = fireIncident.isPeopleInVicinity();
        this.proximityOfPeopleToFire = fireIncident.getProximityOfPeopleToFire();
        this.fireCompany = new FireCompanyDataDTO(fireIncident.getFireCompany());
        this.finished = fireIncident.isFinished();
    }

    public FireIncidentDTO() {
    }

    public FireIncidentDTO(Long id, BurningMatter matter, StructureType structure, FlamesType flames, Double volume, SmokeType smoke, Double windSpeed, WindDirection windDirection, Double proximityToResidentialArea, RoomPlacement roomPlacement, Double voltage, boolean peopleInVicinity, Double proximityOfPeopleToFire, FireCompanyDataDTO fireCompany, boolean finished) {
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
        this.fireCompany = fireCompany;
        this.finished = finished;
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

    public FireCompanyDataDTO getFireCompany() {
        return fireCompany;
    }

    public void setFireCompany(FireCompanyDataDTO fireCompany) {
        this.fireCompany = fireCompany;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
