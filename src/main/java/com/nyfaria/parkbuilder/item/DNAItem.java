package com.nyfaria.parkbuilder.item;

import net.minecraft.world.item.Item;

public class DNAItem extends Item {

    private final Item egg;
    public DNAItem(Item egg, Properties properties) {
        super(properties);
        this.egg = egg;
    }

    public Item getEgg() {
        return egg;
    }
}
