package com.ftn.sbnz.model.models;

import org.kie.api.definition.type.Position;

import java.util.Objects;

public class ValidStep {
    @Position(0)
    private String step;

    @Position(1)
    private String nextStep;

    public ValidStep() {
    }

    public ValidStep(String step, String nextStep) {
        this.step = step;
        this.nextStep = nextStep;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidStep validStep = (ValidStep) o;
        return step.equals(validStep.step) && nextStep.equals(validStep.nextStep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(step, nextStep);
    }

    @Override
    public String toString() {
        return "ValidStep{" +
                "step='" + step + '\'' +
                ", nextStep='" + nextStep + '\'' +
                '}';
    }
}