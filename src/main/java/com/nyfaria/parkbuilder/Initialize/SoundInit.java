package com.nyfaria.parkbuilder.Initialize;

import com.nyfaria.parkbuilder.ParkBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundInit {

    public static final SoundEvent TRICERATOPS_AMBIENT;

    public static final SoundEvent TRICERATOPS_HURT;

    public static final SoundEvent TRICERATOPS_DEATH;

    public static final SoundEvent TRICERATOPS_FOOTSTEP;

    public static final SoundEvent VELOCIRAPTOR_M_AMBIENT;

    public static final SoundEvent VELOCIRAPTOR_M_HURT;

    public static final SoundEvent VELOCIRAPTOR_M_DEATH;

    public static final SoundEvent VELOCIRAPTOR_A_HURT;

    public static final SoundEvent VELOCIRAPTOR_A_DEATH;

    public static final SoundEvent VELOCIRAPTOR_A_AMBIENT;

    public static final SoundEvent DRYOSAURUS_AMBIENT;

    public static final SoundEvent DRYOSAURUS_HURT;

    public static final SoundEvent DRYOSAURUS_DEATH;

    public static final SoundEvent BARYONYX_AMBIENT;

    public static final SoundEvent BARYONYX_DEATH;

    public static final SoundEvent BARYONYX_HURT;

    public static void init() {
    }

    private static SoundEvent createSoundEvent(String soundName) {
        return Registry.register(Registry.SOUND_EVENT, ParkBuilder.modLoc(soundName),
            new SoundEvent(new ResourceLocation("parkbuilder", soundName)));

    }

    static {
        TRICERATOPS_AMBIENT = createSoundEvent("triceratops_ambient");

        TRICERATOPS_HURT = createSoundEvent("triceratops_hurt");

        TRICERATOPS_DEATH = createSoundEvent("triceratops_death");

        TRICERATOPS_FOOTSTEP = createSoundEvent("triceratops_footstep");

        VELOCIRAPTOR_M_AMBIENT = createSoundEvent("velociraptor_m_ambient");

        VELOCIRAPTOR_M_HURT = createSoundEvent("velociraptor_m_hurt");

        VELOCIRAPTOR_M_DEATH = createSoundEvent("velociraptor_m_death");

        VELOCIRAPTOR_A_HURT = createSoundEvent("velociraptor_a_hurt");

        VELOCIRAPTOR_A_DEATH = createSoundEvent("velociraptor_a_death");

        VELOCIRAPTOR_A_AMBIENT = createSoundEvent("velociraptor_a_ambient");

        DRYOSAURUS_AMBIENT = createSoundEvent("dryo_ambient");

        DRYOSAURUS_HURT = createSoundEvent("dryo_hurt");

        DRYOSAURUS_DEATH = createSoundEvent("dryo_death");

        BARYONYX_AMBIENT = createSoundEvent("bary_ambient");

        BARYONYX_DEATH = createSoundEvent("bary_death");

        BARYONYX_HURT = createSoundEvent("bary_hurt");
    }
}

