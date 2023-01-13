package com.nyfaria.parkbuilder;

import com.nyfaria.parkbuilder.Initialize.*;
import com.nyfaria.parkbuilder.block.DinoEggBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParkBuilder implements ModInitializer {
    public static final String MOD_ID = "parkbuilder";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final CreativeModeTab ITEM_GROUP = FabricItemGroupBuilder.build(new ResourceLocation(MOD_ID, "main"), () -> new ItemStack(ItemInit.PARK_BUILDER));


    @Override
    public void onInitialize() {
        DataSerializerInit.init();
        EntityInit.init();
        BlockInit.init();
        ItemInit.init();
        SoundInit.init();
        ContainerInit.init();
        TagInit.init();
        WorldGenInit.init();
        DinoEggBlock.CommonHandler.setupEggs();

    }

    public static ResourceLocation modLoc(String name) {
        return new ResourceLocation(ParkBuilder.MOD_ID, name);
    }
}
