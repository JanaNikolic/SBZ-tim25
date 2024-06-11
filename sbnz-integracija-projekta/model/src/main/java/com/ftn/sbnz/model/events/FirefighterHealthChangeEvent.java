package com.ftn.sbnz.model.events;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.util.Date;
import java.util.Objects;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
@Expires("2m")
public class FirefighterHealthChangeEvent {
    private Long firefighterId;
    private double oxygenLevel;
    private int heartRate;
    private Date timestamp;

    public FirefighterHealthChangeEvent() {
    }

    public FirefighterHealthChangeEvent(Long firefighterId, double oxygenLevel, int heartRate) {
        this.firefighterId = firefighterId;
        this.oxygenLevel = oxygenLevel;
        this.heartRate = heartRate;
        this.timestamp = new Date();
    }

    public Long getFirefighterId() {
        return firefighterId;
    }

    public void setFirefighterId(Long firefighterId) {
        this.firefighterId = firefighterId;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirefighterHealthChangeEvent that = (FirefighterHealthChangeEvent) o;
        return Double.compare(that.oxygenLevel, oxygenLevel) == 0 && heartRate == that.heartRate && Objects.equals(firefighterId, that.firefighterId) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firefighterId, oxygenLevel, heartRate, timestamp);
    }
}

