package com.ftn.sbnz.model.models;

import java.io.Serializable;
import java.util.Objects;

public class Firefighter implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private int age;
    private double oxygenLevel;
    private int heartRate;
    private Long fireId;

    public Firefighter() {
    }

    public Firefighter(Long id, String name, int age, double oxygenLevel, int heartRate, Long fireId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.oxygenLevel = oxygenLevel;
        this.heartRate = heartRate;
        this.fireId = fireId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(double oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public Long getFireId() {
        return fireId;
    }

    public void setFireId(Long fireId) {
        this.fireId = fireId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Firefighter that = (Firefighter) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Firefighter{" +
                "id=" + id +
                ", oxygenLevel=" + oxygenLevel +
                ", heartRate=" + heartRate +
                '}';
    }
}

