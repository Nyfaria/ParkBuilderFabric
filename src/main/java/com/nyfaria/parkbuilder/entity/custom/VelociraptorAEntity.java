package com.nyfaria.parkbuilder.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.nyfaria.parkbuilder.Initialize.SoundInit;
import com.nyfaria.parkbuilder.entity.ai.goal.AnimatedMeleeAttackGoal;
import com.nyfaria.parkbuilder.entity.ai.goal.AttackForFood;
import com.nyfaria.parkbuilder.entity.ai.iface.IHasAnimatedMeleeAttack;
import com.nyfaria.parkbuilder.entity.ai.nums.GrowthStages;
import com.nyfaria.parkbuilder.entity.dinos.DinoBase;
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

public class VelociraptorAEntity extends DinoBase implements IAnimatable, IHasAnimatedMeleeAttack {
    private static final EntityDataAccessor<Integer> ATTACK_TICKS = SynchedEntityData.defineId(VelociraptorAEntity.class, EntityDataSerializers.INT);
    private AnimationFactory factory = new AnimationFactory(this);
    private Map<GrowthStages, Map<Attribute, AttributeModifier>> GROWTH_ATTRIBUTES = Map.of(
            GrowthStages.JUVENILE, Map.of(
                    Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("372636ca-fced-4341-bcf7-08cab02214d4"), "Juvenile Health", 10D, AttributeModifier.Operation.ADDITION)
            ),
            GrowthStages.TEEN, Map.of(
                    Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("e8a403be-10f1-4d36-8107-63200a9b6378"), "teen Health", 10D, AttributeModifier.Operation.ADDITION)
            ),
            GrowthStages.ADULT, Map.of(
                    Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("c9b2efda-ba2f-43a8-91db-71ccb59319a4"), "adult Health", 10D, AttributeModifier.Operation.ADDITION)
            )
    );

    public VelociraptorAEntity(EntityType<? extends DinoBase> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder attributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.00D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1D);

    }
    private Map<GrowthStages, EntityDimensions> DIMENSIONS = Map.of(
            GrowthStages.HATCHLING, EntityDimensions.fixed(0.45f, 0.6f),
            GrowthStages.JUVENILE, EntityDimensions.fixed(0.9F, 1.2F),
            GrowthStages.TEEN, EntityDimensions.fixed(1.35F, 1.8F),
            GrowthStages.ADULT, EntityDimensions.fixed(1.5F, 2.0F)
    );
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(6, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(1, new AnimatedMeleeAttackGoal<>(this, 1.0, false, 20));
        this.goalSelector.addGoal(3, new AttackForFood(this, 1, false));
        this.goalSelector.addGoal(1,new NearestAttackableTargetGoal<>(this,LivingEntity.class,true));
        this.goalSelector.addGoal(7, new DoorInteractGoal(this) {
            @Override
            protected boolean isOpen() {
                return super.isOpen();
            }
        });
    }

    @Override
    public void tick() {
        if (entityData.get(ATTACK_TICKS) > 0)
            entityData.set(ATTACK_TICKS, entityData.get(ATTACK_TICKS) - 1);
        super.tick();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.currentlyAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bite", true));
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            if (isSprinting()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("run", true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
            }
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
    public void setTarget(@Nullable LivingEntity target) {
        setSprinting(target != null);
        super.setTarget(target);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundInit.VELOCIRAPTOR_A_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundInit.VELOCIRAPTOR_A_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundInit.VELOCIRAPTOR_A_DEATH;
    }

    protected float getSoundVolume() {
        return 0.5F;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
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
        return 10;
    }

    @Override
    public int getHurtTicks(int attackNumber) {
        return 7;
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
        if(pTarget instanceof DryosaurusEntity)
            return true;
        return super.canAttack(pTarget);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return DIMENSIONS.get(getGrowth());
    }
}

