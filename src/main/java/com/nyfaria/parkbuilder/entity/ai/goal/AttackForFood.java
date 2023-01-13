package com.nyfaria.parkbuilder.entity.ai.goal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;


public class AttackForFood extends NearestAttackableTargetGoal {

    private final int animTickLength = 12;
    private int animCounter = 0;
    private int TicksSinceKilled;

    public AttackForFood(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, Animal.class,followingTargetEvenIfNotSeen);
        if (this.TicksSinceKilled > 12000) {
//            entity.setAttacking(true);
            animCounter = 0;
            TicksSinceKilled=0;
        }

    }

    protected Predicate<LivingEntity> getPreySelection(Entity entity) {
        return (e) -> e.getType() != entity.getType() && (e.getType() == EntityType.SHEEP || e.getType() == EntityType.RABBIT
                || e.getType() == EntityType.COW || e.getType() == EntityType.CHICKEN || e.getType() == EntityType.PIG);
    }
    public boolean isFood(ItemStack stack){
        Item item = stack.getItem();
        return item.isEdible() && item.getFoodProperties().isMeat();
    }

}
