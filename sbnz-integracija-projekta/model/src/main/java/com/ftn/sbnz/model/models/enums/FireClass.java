package com.ftn.sbnz.model.models.enums;

public enum FireClass {
    ClassA("Solid materials such as wood or paper, fabric, and some plastics"),
    ClassB("Liquids such as solvents, alcohol, fuels and other chemicals and fluids"),
    ClassC("Gases such as butane, methane and propane"),
    ClassD("Metallic substances such as sodium, titanium, zirconium, or magnesium"),
    ClassE("Electrical failure from appliances, electronic equipment, and wiring");

    private final String description;
    FireClass(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}