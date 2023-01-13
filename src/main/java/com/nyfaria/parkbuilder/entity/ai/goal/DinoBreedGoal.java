package com.nyfaria.parkbuilder.entity.ai.goal;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import com.nyfaria.parkbuilder.entity.ai.nums.Gender;
import com.nyfaria.parkbuilder.entity.dinos.DinoBase;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class DinoBreedGoal extends Goal {
        private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight();
        protected final DinoBase dinoBase;
        private final Class<? extends DinoBase> partnerClass;
        protected final Level level;
        @Nullable
        protected DinoBase partner;
        private int loveTime;
        private final double speedModifier;

        public DinoBreedGoal(DinoBase pDinoBase, double pSpeedModifier) {
            this(pDinoBase, pSpeedModifier, pDinoBase.getClass());
        }

        public DinoBreedGoal(DinoBase pDinoBase, double pSpeedModifier, Class<? extends DinoBase> pPartnerClass) {
            this.dinoBase = pDinoBase;
            this.level = pDinoBase.level;
            this.partnerClass = pPartnerClass;
            this.speedModifier = pSpeedModifier;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            if (!this.dinoBase.isInLove()) {
                return false;
            } else {
                this.partner = this.getFreePartner();
                return this.partner != null;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return this.partner.isAlive() && this.partner.isInLove() && this.loveTime < 60 && dinoBase.getWooHooCooldown() == 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void stop() {
            this.partner = null;
            this.loveTime = 0;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            this.dinoBase.getLookControl().setLookAt(this.partner, 10.0F, (float)this.dinoBase.getMaxHeadXRot());
            this.dinoBase.getNavigation().moveTo(this.partner, this.speedModifier);
            ++this.loveTime;
            if (this.loveTime >= this.adjustedTickDelay(60) && this.dinoBase.distanceToSqr(this.partner) < 9.0D) {
                this.breed();
            }

        }

        /**
         * Loops through nearby dinoBases and finds another dinoBase of the same type that can be mated with. Returns the first
         * valid mate found.
         */
        @Nullable
        private DinoBase getFreePartner() {
            List<? extends DinoBase> list = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, this.dinoBase, this.dinoBase.getBoundingBox().inflate(8.0D))
                    .stream().filter(entity -> entity.getGender() != dinoBase.getGender()).toList();
            double d0 = Double.MAX_VALUE;
            DinoBase dinoBase = null;

            for(DinoBase dinoBase1 : list) {
                if (this.dinoBase.canMate(dinoBase1) && this.dinoBase.distanceToSqr(dinoBase1) < d0) {
                    dinoBase = dinoBase1;
                    d0 = this.dinoBase.distanceToSqr(dinoBase1);
                }
            }

            return dinoBase;
        }

        /**
         * Spawns a baby dinoBase of the same type.
         */
        protected void breed() {
            if(this.dinoBase.getGender() == Gender.FEMALE) {
                this.dinoBase.setHasEgg(true);
            }
            dinoBase.setWooHooCooldown(dinoBase.getMaxWooHooCooldown());
            this.dinoBase.resetLove();
            this.partner.resetLove();
            RandomSource randomsource = this.dinoBase.getRandom();
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.level.addFreshEntity(new ExperienceOrb(this.level, this.dinoBase.getX(), this.dinoBase.getY(), this.dinoBase.getZ(), randomsource.nextInt(7) + 1));
            }
        }

}
