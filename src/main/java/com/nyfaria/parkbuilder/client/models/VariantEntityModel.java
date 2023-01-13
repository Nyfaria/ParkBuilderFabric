package com.nyfaria.parkbuilder.client.models;

import com.nyfaria.parkbuilder.entity.dinos.DinoBase;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.HashMap;
import java.util.Map;

public class VariantEntityModel<T extends DinoBase> extends AnimatedGeoModel<T> {

    protected final Map<ResourceLocation, ResourceLocation> geoCache = new HashMap<>();
    protected final Map<ResourceLocation, ResourceLocation> animationCache = new HashMap<>();
    protected final Map<String, ResourceLocation> textureCache = new HashMap<>();

    @Override
    public ResourceLocation getModelResource(T object) {
        return geoCache.computeIfAbsent(Registry.ENTITY_TYPE.getKey(object.getType()),
                k -> new ResourceLocation(k.getNamespace(), "geo/" + k.getPath() + ".geo.json"));
    }

    @Override
    public ResourceLocation getTextureResource(T object) {

        return textureCache.computeIfAbsent(Registry.ENTITY_TYPE.getKey(object.getType()).getPath() + "_" + object.getGenderName(),
                k -> new ResourceLocation(Registry.ENTITY_TYPE.getKey(object.getType()).getNamespace(), "textures/entity/" + Registry.ENTITY_TYPE.getKey(object.getType()).getPath() + "/" + k + ".png"));
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return animationCache.computeIfAbsent(Registry.ENTITY_TYPE.getKey(animatable.getType()),
                k -> new ResourceLocation(k.getNamespace(), "animations/" + k.getPath() + ".animation.json"));
    }
}
