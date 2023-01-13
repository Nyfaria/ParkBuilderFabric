package com.nyfaria.parkbuilder.Initialize;

import com.nyfaria.parkbuilder.ParkBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ItemInit {


    public static final Item AMBER =
            new Item(getItemProperties());

    private static<T extends Item> T register(String name, T o) {
        return Registry.register(Registry.ITEM, ParkBuilder.modLoc( name), o);
    }

    public static final Item AMBER_MOSQUITO =
            new Item(getItemProperties());

    public static final Item FOSSIL =
            new Item(getItemProperties());

    public static final Item MIRACLE_BERRY =
            new Item(getItemProperties());
    public static final Item TRANSDINOFICATOR =
            new Item(getItemProperties());

    public static final Item PARK_BUILDER =
            new Item(new FabricItemSettings());
    public static Item.Properties getItemProperties() {
        return new FabricItemSettings().tab(ParkBuilder.ITEM_GROUP);
    }

    public static void init() {
        register("amber",ItemInit.AMBER);
        register("amber_mosquito",ItemInit.AMBER_MOSQUITO);
        register("fossil",ItemInit.FOSSIL);
        register("miracle_berry",ItemInit.MIRACLE_BERRY);
        register("transdinoficator",ItemInit.TRANSDINOFICATOR);
        register("park_builder",ItemInit.PARK_BUILDER);
    }
}

