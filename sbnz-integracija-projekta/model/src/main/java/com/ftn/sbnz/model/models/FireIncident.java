package com.ftn.sbnz.model.models;

import com.ftn.sbnz.model.models.enums.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class FireIncident implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BurningMatter matter;
    @Column(nullable = false)
    private StructureType structure;
    @Column(nullable = false)
    private FlamesType flames;
    @NotNull
    @Column(nullable = false)
    private Double volume;
    @Column(nullable = false)
    private SmokeType smoke;
    @NotNull
    @Column(nullable = false)
    private Double windSpeed;
    @Column(nullable = false)
    private WindDirection windDirection;
    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 500)
    private Double proximityToResidentialArea;
    @Column(nullable = false)
    private RoomPlacement roomPlacement;
    @Min(value = 0)
    @Max(value = 100000)
    @Column()
    private Double voltage;
    @Column(nullable = false)
    @NotNull
    private boolean peopleInVicinity;
    @Min(value = 0)
    @Max(value = 500)
    @NotNull
    @Column(nullable = false)
    private Double proximityOfPeopleToFire;

    @OneToOne
    @JoinColumn(name = "firecompany_id", nullable = false)
    private FireCompany fireCompany;

    @Column
    private boolean finished;

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
        this.finished = false;
    }

    public FireIncident(Long id, BurningMatter matter, StructureType structure, FlamesType flames, Double volume, SmokeType smoke, Double windSpeed, WindDirection windDirection, Double proximityToResidentialArea, RoomPlacement roomPlacement, Double voltage, boolean peopleInVicinity, Double proximityOfPeopleToFire, FireCompany fireCompany, boolean finished) {
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

    public FireCompany getFireCompany() {
        return fireCompany;
    }

    public void setFireCompany(FireCompany fireCompany) {
        this.fireCompany = fireCompany;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "FireIncident{" +
                "id=" + id +
                ", matter=" + matter +
                ", structure=" + structure +
                ", flames=" + flames +
                ", volume=" + volume +
                ", smoke=" + smoke +
                ", windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                ", proximityToResidentialArea=" + proximityToResidentialArea +
                ", roomPlacement=" + roomPlacement +
                ", voltage=" + voltage +
                ", peopleInVicinity=" + peopleInVicinity +
                ", proximityOfPeopleToFire=" + proximityOfPeopleToFire +
                ", fireCompany=" + fireCompany +
                ", finished=" + finished +
                '}';
    }
}
