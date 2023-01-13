package com.nyfaria.parkbuilder.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.nyfaria.parkbuilder.Initialize.EntityInit;
import com.nyfaria.parkbuilder.Initialize.ItemInit;
import com.nyfaria.parkbuilder.Initialize.SoundInit;
import com.nyfaria.parkbuilder.entity.ai.nums.GrowthStages;
import com.nyfaria.parkbuilder.entity.dinos.DinoBase;
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
import java.util.function.Predicate;

public class TriceratopsEntity extends DinoBase implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);
    private Map<GrowthStages, Map<Attribute, AttributeModifier>> GROWTH_ATTRIBUTES = Map.of(
            GrowthStages.JUVENILE, Map.of(
                    Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("372636ca-fced-4341-bcf7-08cab02214d4"), "Juvenile Health", 15D, AttributeModifier.Operation.ADDITION)
            ),
            GrowthStages.TEEN, Map.of(
                    Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("e8a403be-10f1-4d36-8107-63200a9b6378"), "teen Health", 50D, AttributeModifier.Operation.ADDITION)
            ),
            GrowthStages.ADULT, Map.of(
                    Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("c9b2efda-ba2f-43a8-91db-71ccb59319a4"), "adult Health", 65D, AttributeModifier.Operation.ADDITION)
            )
    );

    private Map<GrowthStages, EntityDimensions> DIMENSIONS = Map.of(
            GrowthStages.HATCHLING, EntityDimensions.fixed(0.9f, 0.9f),
            GrowthStages.JUVENILE, EntityDimensions.fixed(1.8F, 1.8F),
            GrowthStages.TEEN, EntityDimensions.fixed(2.7F, 2.7F),
            GrowthStages.ADULT, EntityDimensions.fixed(3.0F, 3.0F)
    );

    public TriceratopsEntity(EntityType<? extends DinoBase> entityType, Level level) {
        super(entityType, level);
    }


    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return DIMENSIONS.get(getGrowth());
    }

    public static AttributeSupplier.Builder attributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 15.00D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 18.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 5D);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new FloatGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(6, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new DontFuckWithMahBabyGoal());
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 3, false));
        this.goalSelector.addGoal(2, new EatBlockGoal(this));
    }




    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
            return PlayState.CONTINUE;
        }
        if (this.isAggressive() && !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("charge", true));
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
    public boolean isFood(ItemStack pStack) {

        return pStack.getItem() == ItemInit.AMBER;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public TriceratopsEntity getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {

        return EntityInit.TRICERATOPS.create(serverLevel);
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
        this.playSound(SoundInit.TRICERATOPS_FOOTSTEP, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundInit.TRICERATOPS_AMBIENT;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return SoundInit.TRICERATOPS_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundInit.TRICERATOPS_DEATH;
    }


    protected float getSoundVolume() {
        return 0.5F;
    }




    @Override
    public Map<Attribute, AttributeModifier> getAttributeModifiers() {
        return GROWTH_ATTRIBUTES.get(getGrowth());
    }

    class DontFuckWithMahBabyGoal extends NearestAttackableTargetGoal<Player> {
        public DontFuckWithMahBabyGoal() {
            super(TriceratopsEntity.this, Player.class, 20, true, true, (Predicate<LivingEntity>)null);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (TriceratopsEntity.this.getGrowth() != GrowthStages.ADULT) {
                return false;
            } else {
                if (super.canUse()) {
                    for(TriceratopsEntity polarbear : TriceratopsEntity.this.level.getEntitiesOfClass(TriceratopsEntity.class, TriceratopsEntity.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
                        if (polarbear.getGrowth() != GrowthStages.ADULT) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }

        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5D;
        }
    }
}





