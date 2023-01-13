package com.nyfaria.parkbuilder.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.nyfaria.parkbuilder.client.models.VariantEntityModel;
import com.nyfaria.parkbuilder.entity.dinos.DinoBase;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class VariantEntityRenderer<T extends DinoBase> extends GeoEntityRenderer<T> {

    private final float baseShadowSize;

    public VariantEntityRenderer(EntityRendererProvider.Context renderManager) {
        this(renderManager, 1f);
    }

    public VariantEntityRenderer(EntityRendererProvider.Context renderManager, float shadowSize) {
        super(renderManager, new VariantEntityModel<>());
        this.shadowRadius = shadowSize;
        this.baseShadowSize = shadowSize;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        float scale = entity.getGrowth().getScale();
        stack.scale(scale, scale, scale);
        this.shadowRadius = baseShadowSize * scale;
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.scale(1f / scale, 1f / scale, 1f / scale);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return null;
    }

}
