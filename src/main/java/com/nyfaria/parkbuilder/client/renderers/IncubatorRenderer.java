package com.nyfaria.parkbuilder.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import com.nyfaria.parkbuilder.block.BottomTopBlock;
import com.nyfaria.parkbuilder.block.entity.incubator.IncubatorBlockEntity;

public class IncubatorRenderer implements BlockEntityRenderer<IncubatorBlockEntity> {
    public IncubatorRenderer() {
        super();
    }

    @Override
    public void render(IncubatorBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
//        if(pBlockEntity.getBlockState().getValue(BottomTopBlock.HALF) == DoubleBlockHalf.UPPER) {
//            return;
//        }
        ItemStack toRender = (!pBlockEntity.getItem(2).isEmpty()) ? pBlockEntity.getItem(2) : pBlockEntity.getItem(1);
        if(!toRender.isEmpty()){
            pPoseStack.translate(0.5f,1.6f,0.5f);
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees((pBlockEntity.getLevel().getGameTime() % 360) * 4));
            int lightAbove = LevelRenderer.getLightColor(Minecraft.getInstance().level, pBlockEntity.getBlockPos().above(2));
            Minecraft.getInstance().getItemRenderer().renderStatic(toRender, ItemTransforms.TransformType.GROUND, lightAbove , pPackedOverlay, pPoseStack, pBufferSource,0);
        }
    }
}
