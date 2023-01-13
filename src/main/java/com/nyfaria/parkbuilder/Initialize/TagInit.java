package com.nyfaria.parkbuilder.Initialize;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import com.nyfaria.parkbuilder.ParkBuilder;

public class TagInit {



    public static TagKey<Item> EXTRACTABLES = itemTag("can_extract");
    public static TagKey<Item> CATALYSTS = itemTag("is_cultivator_catalyst");


    public static void init() {
    }


    public static TagKey<Block> blockTag(String path) {
        return TagKey.create(Registry.BLOCK_REGISTRY,new ResourceLocation(ParkBuilder.MOD_ID, path));
    }

    public static TagKey<Item> itemTag(String path) {
        return TagKey.create(Registry.ITEM_REGISTRY,new ResourceLocation(ParkBuilder.MOD_ID, path));
    }


}
