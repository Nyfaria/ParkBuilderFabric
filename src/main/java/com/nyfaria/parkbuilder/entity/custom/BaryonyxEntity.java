package com.nyfaria.parkbuilder.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.nyfaria.parkbuilder.Initialize.SoundInit;
import com.nyfaria.parkbuilder.entity.ai.goal.AnimatedMeleeAttackGoal;
import com.nyfaria.parkbuilder.entity.ai.goal.AttackForFood;
import com.nyfaria.parkbuilder.entity.ai.iface.IHasAnimatedMeleeAttack;
import com.nyfaria.parkbuilder.entity.ai.nums.GrowthStages;
import com.nyfaria.parkbuilder.entity.dinos.AquaticCarnivore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Map;
import java.util.UUID;

public class BaryonyxEntity extends AquaticCarnivore implements IHasAnimatedMeleeAttack {

    private boolean isBasking;

    private static final EntityDataAccessor<Integer> ATTACK_TICKS = SynchedEntityData.defineId(BaryonyxEntity.class, EntityDataSerializers.INT);
    private final AnimationFactory factory = new AnimationFactory(this);
    private Map<GrowthStages, Map<Attribute, AttributeModifier>> GROWTH_ATTRIBUTES = Map.of(
            GrowthStages.JUVENILE, Map.of(
                    Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("372636ca-fced-4341-bcf7-08cab02214d4"), "Juvenile Health", 16D, AttributeModifier.Operation.ADDITION)
            ),
            GrowthStages.TEEN, Map.of(
                    Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("e8a403be-10f1-4d36-8107-63200a9b6378"), "teen Health", 21D, AttributeModifier.Operation.ADDITION)
            ),
            GrowthStages.ADULT, Map.of(
                    Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("c9b2efda-ba2f-43a8-91db-71ccb59319a4"), "adult Health", 20D, AttributeModifier.Operation.ADDITION)
            )
    );

    public BaryonyxEntity(EntityType<? extends AquaticCarnivore> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder attributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.00D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 9.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1D);

    }
    private Map<GrowthStages, EntityDimensions> DIMENSIONS = Map.of(
            GrowthStages.HATCHLING, EntityDimensions.fixed(0.6f, 0.6f),
            GrowthStages.JUVENILE, EntityDimensions.fixed(1.2F, 1.2F),
            GrowthStages.TEEN, EntityDimensions.fixed(1.8F, 1.8F),
            GrowthStages.ADULT, EntityDimensions.fixed(2.0F, 2.0F)
    );
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(6, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(1, new AnimatedMeleeAttackGoal<>(this, 1.0, false, 20));
        this.goalSelector.addGoal(1,new NearestAttackableTargetGoal<>(this, LivingEntity.class,true));
        this.goalSelector.addGoal(3, new AttackForFood(this, 1, false));
    }

    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) return;

        if (entityData.get(ATTACK_TICKS) > 0) {
            entityData.set(ATTACK_TICKS, entityData.get(ATTACK_TICKS) - 1);
        }
    }
    public boolean isBasking() {
        return this.getSharedFlag(3);
    }


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.currentlyAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack1", true));
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            if (isSprinting()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("run", true));
            } else if (isInWater()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("swim", true));
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
            }
            return PlayState.CONTINUE;
        }
        if (isBasking()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bask", true));
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
    public void setTarget(@Nullable LivingEntity target) {
        setSprinting(target != null);
        super.setTarget(target);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
        this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundInit.BARYONYX_AMBIENT;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return SoundInit.BARYONYX_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundInit.BARYONYX_DEATH;
    }


    protected float getSoundVolume() {
        return 0.5F;
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_TICKS, 0);
    }


    @Override
    public int getAttackTicks(int attackNumber) {
        return entityData.get(ATTACK_TICKS);
    }

    @Override
    public void setAttackTicks(int attackTicks, int attackNumber) {
        entityData.set(ATTACK_TICKS, attackTicks);
    }

    @Override
    public int getTotalAttackTicks(int attackNumber) {
        return 19;
    }

    @Override
    public int getHurtTicks(int attackNumber) {
        return 12;
    }

    @Override
    public void doAttackStuff(int attackNumber) {

    }

    @Override
    public boolean currentlyAttacking() {
        return entityData.get(ATTACK_TICKS) > 0;
    }

    @Override
    public Map<Attribute, AttributeModifier> getAttributeModifiers() {
        return GROWTH_ATTRIBUTES.get(getGrowth());
    }

    @Override
    public boolean canAttack(LivingEntity pTarget) {
        if(pTarget instanceof TriceratopsEntity triceratopsEntity)
            return triceratopsEntity.getGrowth() != GrowthStages.ADULT;
        return super.canAttack(pTarget);
    }
}

