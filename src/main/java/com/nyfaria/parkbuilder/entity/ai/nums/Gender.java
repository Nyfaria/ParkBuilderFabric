package com.nyfaria.parkbuilder.entity.ai.nums;


import com.nyfaria.parkbuilder.entity.ai.iface.IVariant;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

public enum Gender implements IVariant {

    FEMALE(0, "female"),
    MALE(1,"male");


    private static final Gender[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(Gender::getId)).toArray(Gender[]::new);
    private final int id;
    private final String displayName;

    Gender(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return this.id;
    }

    public static Gender byId(int id) {
        return BY_ID[id % BY_ID.length];
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
