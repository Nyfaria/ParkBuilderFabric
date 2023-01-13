package com.nyfaria.parkbuilder.Initialize;

import com.nyfaria.parkbuilder.ParkBuilder;
import com.nyfaria.parkbuilder.block.entity.cultivator.CultivatorMenu;
import com.nyfaria.parkbuilder.block.entity.extractor.ExtractorMenu;
import com.nyfaria.parkbuilder.block.entity.incubator.IncubatorMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

public class ContainerInit {

    public static void init(){
    }

    public static MenuType<ExtractorMenu> EXTRACTOR_MENU = Registry.register(Registry.MENU, ParkBuilder.modLoc("extractor_menu"), new ExtendedScreenHandlerType<>(ExtractorMenu::new));
    public static MenuType<CultivatorMenu> CULTIVATOR_MENU = Registry.register(Registry.MENU,ParkBuilder.modLoc("cultivator_menu"), new ExtendedScreenHandlerType<>(CultivatorMenu::new));
    public static MenuType<IncubatorMenu> INCUBATOR_MENU = Registry.register(Registry.MENU,ParkBuilder.modLoc("incubator_menu"), new ExtendedScreenHandlerType<>(IncubatorMenu::new));
}
