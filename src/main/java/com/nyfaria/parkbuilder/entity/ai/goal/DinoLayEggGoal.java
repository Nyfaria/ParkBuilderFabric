package com.nyfaria.parkbuilder.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import com.nyfaria.parkbuilder.block.DinoEggBlock;
import com.nyfaria.parkbuilder.entity.dinos.DinoBase;

public class DinoLayEggGoal extends MoveToBlockGoal {
      private final DinoBase dino;

      public DinoLayEggGoal(DinoBase dino, double p_30277_) {
         super(dino, p_30277_, 16);
         this.dino = dino;
      }

      /**
       * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
       * method as well.
       */
      public boolean canUse() {
         return this.dino.hasEgg() && super.canUse();
      }

      /**
       * Returns whether an in-progress EntityAIBase should continue executing
       */
      public boolean canContinueToUse() {
         return super.canContinueToUse() && this.dino.hasEgg();
      }

      /**
       * Keep ticking a continuous task that has already been started
       */
      public void tick() {
         super.tick();
         BlockPos blockpos = this.dino.blockPosition();
         if (!this.dino.isInWater() && this.isReachedTarget()) {
            if (this.dino.layEggCounter < 1) {
               this.dino.setLayingEgg(true);
            } else if (this.dino.layEggCounter > this.adjustedTickDelay(200)) {
               Level level = this.dino.level;
               level.playSound((Player)null, blockpos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
               level.setBlock(this.blockPos.above(), dino.getEggBlock().defaultBlockState().setValue(DinoEggBlock.EGGS, this.dino.getRandom().nextInt(4) + 1), 3);
               this.dino.setHasEgg(false);
               this.dino.setLayingEgg(false);
               this.dino.setInLoveTime(600);
            }

            if (this.dino.isLayingEgg()) {
               ++this.dino.layEggCounter;
            }
         }

      }

      /**
       * Return true to set given position as destination
       */
      protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
         return pLevel.isEmptyBlock(pPos.above()) && DinoEggBlock.isSand(pLevel, pPos);
      }
   }
