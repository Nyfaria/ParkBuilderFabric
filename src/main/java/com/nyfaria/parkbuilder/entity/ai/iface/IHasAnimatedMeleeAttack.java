package com.nyfaria.parkbuilder.entity.ai.iface;

public interface IHasAnimatedMeleeAttack {
    int getAttackTicks(int attackNumber);

    void setAttackTicks(int attackTicks, int attackNumber);

    int getTotalAttackTicks(int attackNumber);

    int getHurtTicks(int attackNumber);

    void doAttackStuff(int attackNumber);

    boolean currentlyAttacking();
}
