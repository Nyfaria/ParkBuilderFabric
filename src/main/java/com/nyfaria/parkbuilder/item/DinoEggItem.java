package com.nyfaria.parkbuilder.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;

public class DinoEggItem extends ItemNameBlockItem {

    private final Item hatchedEgg;
    public DinoEggItem(Item hatchedEgg, Block block, Properties properties) {
        super(block,properties);
        this.hatchedEgg = hatchedEgg;
    }
    public Item getHatchedEgg() {
        return hatchedEgg;
    }
}
