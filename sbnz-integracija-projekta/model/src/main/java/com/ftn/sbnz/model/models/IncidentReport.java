package com.ftn.sbnz.model.models;

import java.io.Serializable;
import java.util.List;

public class IncidentReport implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Step> steps;
    private int fireId;

    public IncidentReport() {}

    public IncidentReport(List<Step> steps, int fireId) {
        this.steps = steps;
        this.fireId = fireId;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Step getFirst() {return this.steps.get(0);}

    public int getFireId() {
        return fireId;
    }

    public void setFireId(int fireId) {
        this.fireId = fireId;
    }

    @Override
    public String toString() {
        return "IncidentReport{" +
                "steps=" + steps +
                ", fireId=" + fireId +
                '}';
    }
}