package com.nyfaria.parkbuilder.Initialize;

import com.nyfaria.parkbuilder.entity.ai.nums.Gender;
import com.nyfaria.parkbuilder.entity.ai.nums.GrowthStages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class DataSerializerInit {
    public static void init(){
        EntityDataSerializers.registerSerializer(GROWTH);
        EntityDataSerializers.registerSerializer(GENDER);
    }
    public static final EntityDataSerializer<GrowthStages> GROWTH =   createEnumDataSerializer(GrowthStages.class);;
    public static final EntityDataSerializer<Gender> GENDER =  createEnumDataSerializer(Gender.class);;

    public static <E extends Enum<E>> EntityDataSerializer<E> createEnumDataSerializer(Class<E> enumClass) {
        return new EntityDataSerializer<E>() {
            @Override
            public void write(FriendlyByteBuf buffer, E value) {
                buffer.writeEnum(value);
            }

            @Override
            public E read(FriendlyByteBuf buffer) {
                return buffer.readEnum(enumClass);
            }

            @Override
            public E copy(E value) {
                return value;
            }
        };
    }
}
