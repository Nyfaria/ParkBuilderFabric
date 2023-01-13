package com.nyfaria.parkbuilder.entity.custom;

import com.nyfaria.parkbuilder.Initialize.SoundInit;
import com.nyfaria.parkbuilder.entity.ai.nums.GrowthStages;
import com.nyfaria.parkbuilder.entity.dinos.DinoBase;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Map;
import java.util.UUID;

public class DryosaurusEntity extends DinoBase implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private Map<GrowthStages, Map<Attribute, AttributeModifier>> GROWTH_ATTRIBUTES = Map.of(
            GrowthStages.JUVENILE, Map.of(
                    Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("372636ca-fced-4341-bcf7-08cab02214d4"), "Juvenile Health", 3D, AttributeModifier.Operation.ADDITION)
            ),
            GrowthStages.TEEN, Map.of(
                    Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("e8a403be-10f1-4d36-8107-63200a9b6378"), "teen Health", 6D, AttributeModifier.Operation.ADDITION)
            ),
            GrowthStages.ADULT, Map.of(
                    Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("c9b2efda-ba2f-43a8-91db-71ccb59319a4"), "adult Health", 8D, AttributeModifier.Operation.ADDITION)
            )
    );
    public DryosaurusEntity(EntityType<? extends DinoBase> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder attributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3.00D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 2D);

    }
    private Map<GrowthStages, EntityDimensions> DIMENSIONS = Map.of(
            GrowthStages.HATCHLING, EntityDimensions.fixed(0.45f, 0.6f),
            GrowthStages.JUVENILE, EntityDimensions.fixed(0.9F, 1.2F),
            GrowthStages.TEEN, EntityDimensions.fixed(1.35F, 1.8F),
            GrowthStages.ADULT, EntityDimensions.fixed(1.5F, 2.0F)
    );
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new EatBlockGoal(this));
    }

    private int eatAnimationTick;


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
            return PlayState.CONTINUE;
        }
        if (this.eatAnimationTick > 4 && this.eatAnimationTick <= 36) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("specialidle", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        return PlayState.CONTINUE;
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }


    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundInit.DRYOSAURUS_AMBIENT;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return SoundInit.DRYOSAURUS_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundInit.DRYOSAURUS_HURT;
    }


    protected float getSoundVolume() {
        return 0.5F;
    }

    @Override
    public Map<Attribute, AttributeModifier> getAttributeModifiers() {
        return GROWTH_ATTRIBUTES.get(getGrowth());
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return DIMENSIONS.get(getGrowth());
    }
}





