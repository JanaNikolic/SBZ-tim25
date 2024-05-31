package com.ftn.sbnz.model.models;

import org.kie.api.definition.type.Position;

import java.util.Objects;

public class Step {
    @Position(0)
    private String name;
    @Position(1)
    private int order;

    public Step() {}

    public Step(String name, int order) {
        this.name = name;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Step{" +
                "name='" + name + '\'' +
                ", order=" + order +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Step step = (Step) o;
        return order == step.order && name.equals(step.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, order);
    }
}