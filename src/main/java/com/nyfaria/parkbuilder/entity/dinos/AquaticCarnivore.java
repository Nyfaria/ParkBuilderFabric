package com.nyfaria.parkbuilder.entity.dinos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import com.nyfaria.parkbuilder.entity.ai.goal.TryFindLandGoal;
import com.nyfaria.parkbuilder.entity.ai.movement.AmphibiousLookControl;
import com.nyfaria.parkbuilder.entity.ai.movement.AmphibiousMoveControl;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class AquaticCarnivore extends DinoBase{

    private int modeChangeTicks = 0;
    protected MoveControl waterMoveControl;
    protected LookControl waterLookControl;

    protected AquaticCarnivore(EntityType<? extends DinoBase> p_146738_, Level p_146739_) {
        super(p_146738_, p_146739_);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new AmphibiousMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new AmphibiousLookControl(this, 10);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new BreathAirGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10){
            @Override
            public boolean canUse() {
                return super.canUse() && isInWater();
            }
        });
        this.goalSelector.addGoal(9, new RandomStrollGoal(this,1.0,20){
            @Override
            public boolean canUse() {
                return super.canUse() && !isInWater();
            }
        });
        this.goalSelector.addGoal(10, new TryFindWaterGoal(this){
            @Override
            public void start() {
                super.start();
                modeChangeTicks = 6000;
            }

            @Override
            public boolean canUse() {
                return super.canUse() && modeChangeTicks <= 0;
            }
        });
        this.goalSelector.addGoal(10, new TryFindLandGoal(this){
            @Override
            public void start() {
                super.start();
                modeChangeTicks = 6000;
            }

            @Override
            public boolean canUse() {
                return super.canUse() && modeChangeTicks <= 0;
            }
        });
    }

    public boolean isPushedByFluid() {
        return false;
    }
    @Override
    public void tick() {
        super.tick();
        if(modeChangeTicks > 0) {
            modeChangeTicks--;
        }
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new AmphibiousPathNavigation(this, pLevel);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("modeChangeTicks", this.modeChangeTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.modeChangeTicks = pCompound.getInt("modeChangeTicks");
    }

//    @Override
//    public MoveControl getMoveControl() {
//        if(isInWater())
//            return waterMoveControl;
//        return super.getMoveControl();
//    }
//
//    @Override
//    public LookControl getLookControl() {
//        if(isInWater())
//            return waterLookControl;
//        return super.getLookControl();
//    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }

    protected int increaseAirSupply(int pCurrentAir) {
        return this.getMaxAirSupply();
    }
    @Override
    public int getMaxAirSupply() {
        return 4800;
    }
    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(pTravelVector);
        }

    }
}
