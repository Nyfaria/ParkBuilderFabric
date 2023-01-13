package com.nyfaria.parkbuilder.entity.ai.iface;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import com.nyfaria.parkbuilder.entity.ai.nums.GrowthStages;

import java.util.List;
import java.util.Map;

public interface IAgeable<T extends AgeableMob & IAgeable<T>> {


}
