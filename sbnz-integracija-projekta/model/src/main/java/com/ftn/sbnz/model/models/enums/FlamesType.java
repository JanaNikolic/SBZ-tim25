package com.ftn.sbnz.model.models.enums;

public enum FlamesType {
    HIGH(10), MEDIUM(5), LOW(2);
    private double risk;

    FlamesType(double risk) {
        this.risk = risk;
    }
}
