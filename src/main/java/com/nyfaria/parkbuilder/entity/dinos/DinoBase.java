package com.nyfaria.parkbuilder.entity.dinos;

import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import com.nyfaria.parkbuilder.Initialize.DataSerializerInit;
import com.nyfaria.parkbuilder.Initialize.ItemInit;
import com.nyfaria.parkbuilder.block.DinoEggBlock;
import com.nyfaria.parkbuilder.entity.ai.goal.DinoBreedGoal;
import com.nyfaria.parkbuilder.entity.ai.goal.DinoGetHornyGoal;
import com.nyfaria.parkbuilder.entity.ai.goal.DinoLayEggGoal;
import com.nyfaria.parkbuilder.entity.ai.nums.Gender;
import com.nyfaria.parkbuilder.entity.ai.nums.GrowthStages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Map;

public abstract class DinoBase extends PathfinderMob implements IAnimatable {

    private static final EntityDataAccessor<Gender> GENDER =
            SynchedEntityData.defineId(DinoBase.class, DataSerializerInit.GENDER);
    private static final EntityDataAccessor<GrowthStages> GROWTH_STAGE =
            SynchedEntityData.defineId(DinoBase.class, DataSerializerInit.GROWTH);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(DinoBase.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(DinoBase.class, EntityDataSerializers.BOOLEAN);
    private int age;
    public int layEggCounter;
    private int inLove;
    private int wooHooCooldown;


    protected DinoBase(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public GrowthStages getGrowth() {
        return entityData.get(GROWTH_STAGE);
    }



    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new DinoGetHornyGoal(this));
        goalSelector.addGoal(1, new DinoBreedGoal(this,1.1f));
        goalSelector.addGoal(0, new DinoLayEggGoal(this, 1.0f));
    }

    public void setGrowth(GrowthStages growthStages) {
        entityData.set(GROWTH_STAGE, growthStages);
        this.setAge(growthStages.getTicks());
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_146746_, DifficultyInstance p_146747_,
                                        MobSpawnType p_146748_, @Nullable SpawnGroupData p_146749_,
                                        @Nullable CompoundTag p_146750_) {

        Gender variant = Util.getRandom(Gender.values(), this.random);
        setGender(variant);
        refreshDimensions();
        return super.finalizeSpawn(p_146746_, p_146747_, p_146748_, p_146749_, p_146750_);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("gender", entityData.get(GENDER).getId());
        pCompound.putInt("growth", entityData.get(GROWTH_STAGE).getId());
        pCompound.putInt("age", age);
        pCompound.putBoolean("HasEgg", this.hasEgg());
        pCompound.putInt("InLove", this.inLove);
        pCompound.putInt("WooHooCooldown", this.wooHooCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        entityData.set(GENDER, Gender.byId(pCompound.getInt("gender")));
        entityData.set(GROWTH_STAGE, GrowthStages.byId(pCompound.getInt("growth")));
        age = pCompound.getInt("age");
        this.setHasEgg(pCompound.getBoolean("HasEgg"));
        this.inLove = pCompound.getInt("InLove");
        this.wooHooCooldown = pCompound.getInt("WooHooCooldown");
        refreshDimensions();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GENDER, Gender.MALE);
        this.entityData.define(GROWTH_STAGE, GrowthStages.HATCHLING);
        this.entityData.define(HAS_EGG, false);
        this.entityData.define(LAYING_EGG, false);
    }

    @Override
    public void tick() {
        super.tick();
        calculateGrowth(this);
        if(level.isClientSide)return;
        age++;
        if(wooHooCooldown > 0)
            wooHooCooldown--;
    }

    protected void calculateGrowth(DinoBase entity) {
        GrowthStages growthStages = entity.getGrowth();
        GrowthStages nextGrowthStages = GrowthStages.byId(growthStages.getId() + 1);
        if (nextGrowthStages != null) {
            if (entity.age == nextGrowthStages.getTicks()) {
                if(!level.isClientSide) {
                    setGrowth(nextGrowthStages);
                    updateAttributes(entity);
                }
                refreshDimensions();
            }
        }
    }

    protected void updateAttributes(DinoBase entity) {
        for (Attribute attribute : getAttributeModifiers().keySet()) {
//            entity.getAttribute(attribute).removeModifiers();
            entity.getAttribute(attribute).addPermanentModifier(getAttributeModifiers().get(attribute));
        }
        entity.setHealth(entity.getMaxHealth());
    }

    public Map<Attribute, AttributeModifier> getAttributeModifiers() {
        return null;
    }

    ;

    public String getGenderName() {
        return this.entityData.get(GENDER).getDisplayName();
    }

    public Gender getGender() {
        return this.entityData.get(GENDER);
    }

    public void setGender(Gender gender) {
        this.entityData.set(GENDER, gender);
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public void aiStep() {
        super.aiStep();


        if (this.age < 0) {
            this.inLove = 0;
        }
        if (this.inLove > 0) {
            --this.inLove;
            if (this.inLove % 10 == 0) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
            }
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }

    public void setAge(int age) {
        this.age = age;
    }

    protected boolean isFood(ItemStack pStack) {
        return false;
    }

    public boolean isInLove() {
        if(this.inLove == 0){
            if(getGender() == Gender.FEMALE){
                if(random.nextInt(100) < 20){
                    this.setInLove();
                }
            }
        }
        return this.inLove > 0;
    }
    public void setHasEgg(boolean pHasEgg) {
        this.entityData.set(HAS_EGG, pHasEgg);
    }
    public void setLayingEgg(boolean pIsDigging) {
        this.layEggCounter = pIsDigging ? 1 : 0;
        this.entityData.set(LAYING_EGG, pIsDigging);
    }
    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }


    public void resetLove() {
        this.inLove = 0;
    }
    public boolean canMate(DinoBase pOtherAnimal) {
        if (pOtherAnimal == this) {
            return false;
        } else if (pOtherAnimal.getClass() != this.getClass()) {
            return false;
        } else {
            return this.isInLove() && pOtherAnimal.isInLove();
        }
    }
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            this.inLove = 0;
            return super.hurt(pSource, pAmount);
        }
    }
    public void setInLove() {
        this.inLove = 600;
        this.level.broadcastEntityEvent(this, (byte)18);
    }

    public DinoEggBlock getEggBlock() {
        return DinoEggBlock.fromEntityType(this.getType());
    }
    public void setInLoveTime(int pTicks) {
        this.inLove = pTicks;
    }

    public @NotNull InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(ItemInit.MIRACLE_BERRY)) {
            if(this.getGrowth()!= GrowthStages.ADULT){
                this.setGrowth(GrowthStages.byId(getGrowth().getId() + 1));
                this.updateAttributes(this);
                this.refreshDimensions();
            }
        } else if(itemstack.is(ItemInit.TRANSDINOFICATOR)){
            this.setGender(getGender() == Gender.MALE ? Gender.FEMALE : Gender.MALE);
        }

        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    public boolean canAttack(LivingEntity pTarget) {
        if(pTarget.getClass() == this.getClass())
            return false;

        return super.canAttack(pTarget);
    }
    public int getMaxWooHooCooldown(){
        return 24000;
    }
    public int getWooHooCooldown(){
        return this.wooHooCooldown;
    }



    public void setWooHooCooldown(int maxWooHooCooldown) {
        this.wooHooCooldown = maxWooHooCooldown;
    }

    @Override
    public void checkDespawn() {

    }
}
