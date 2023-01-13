package com.nyfaria.parkbuilder.entity.ai.nums;

public enum GrowthStages {
    HATCHLING(0, "hatchling",-72000, 0.3f),
    JUVENILE(1, "juvenile",-48000, 0.6f),
    TEEN(2, "teen",-12000, 0.9f),
    ADULT(3, "adult",0, 1.0f);

    private final int id;
    private final String displayName;
    private final int ticks;
    private final float scale;
    GrowthStages(int id, String displayName, int ticks, float scale) {
        this.id = id;
        this.displayName = displayName;
        this.ticks = ticks;
        this.scale = scale;
    }

    public int getId() {
        return this.id;
    }

    public static GrowthStages byId(int id) {
        return values()[id % values().length];
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static final int SIZE = values().length;
    public int getTicks() {
        return this.ticks;
    }
    public float getScale() {
        return this.scale;
    }

}
