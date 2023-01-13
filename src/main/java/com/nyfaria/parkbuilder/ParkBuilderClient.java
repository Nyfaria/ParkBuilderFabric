package com.nyfaria.parkbuilder;

import com.nyfaria.parkbuilder.Initialize.BlockInit;
import com.nyfaria.parkbuilder.Initialize.ContainerInit;
import com.nyfaria.parkbuilder.Initialize.EntityInit;
import com.nyfaria.parkbuilder.block.entity.cultivator.CultivatorScreen;
import com.nyfaria.parkbuilder.block.entity.extractor.ExtractorScreen;
import com.nyfaria.parkbuilder.block.entity.incubator.IncubatorScreen;
import com.nyfaria.parkbuilder.client.renderers.CultivatorRenderer;
import com.nyfaria.parkbuilder.client.renderers.IncubatorRenderer;
import com.nyfaria.parkbuilder.client.renderers.VariantEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParkBuilderClient implements ClientModInitializer {
	public static final String MOD_ID = "parkbuilder";
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(EntityInit.TRICERATOPS, (manager) -> new VariantEntityRenderer<>(manager, 3f));
		EntityRendererRegistry.register(EntityInit.VELOCIRAPTORM, VariantEntityRenderer::new);
		EntityRendererRegistry.register(EntityInit.VELOCIRAPTORA, VariantEntityRenderer::new);
		EntityRendererRegistry.register(EntityInit.BARYONYX,VariantEntityRenderer::new);
		EntityRendererRegistry.register(EntityInit.DRYOSAURUS,VariantEntityRenderer::new);
		BlockEntityRendererRegistry.register(BlockInit.INCUBATOR_BLOCK_ENTITY, (a)-> new IncubatorRenderer());
		BlockEntityRendererRegistry.register(BlockInit.CULTIVATOR_BLOCK_ENTITY, (a)-> new CultivatorRenderer());
		MenuScreens.register(ContainerInit.EXTRACTOR_MENU, ExtractorScreen::new);
		MenuScreens.register(ContainerInit.CULTIVATOR_MENU, CultivatorScreen::new);
		MenuScreens.register(ContainerInit.INCUBATOR_MENU, IncubatorScreen::new);
		BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.CULTIVATOR, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.INCUBATOR,RenderType.translucent());
	}
}
