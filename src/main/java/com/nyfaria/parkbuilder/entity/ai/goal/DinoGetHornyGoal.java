package com.nyfaria.parkbuilder.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import com.nyfaria.parkbuilder.entity.ai.nums.Gender;
import com.nyfaria.parkbuilder.entity.dinos.DinoBase;

public class DinoGetHornyGoal extends Goal {

    private DinoBase dinoBase;

    public DinoGetHornyGoal(DinoBase pDinoBase) {
        this.dinoBase = pDinoBase;
    }

    @Override
    public void start() {
        dinoBase.setInLove();
    }

    @Override
    public boolean canUse() {
        return !dinoBase.isInLove() && dinoBase.getGender().equals(Gender.MALE) && dinoBase.getRandom().nextInt(100) < 5 && dinoBase.getWooHooCooldown() == 0;
    }
}
