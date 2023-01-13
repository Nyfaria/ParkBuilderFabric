package com.nyfaria.parkbuilder.Initialize;

import com.nyfaria.parkbuilder.ParkBuilder;
import com.nyfaria.parkbuilder.block.DinoEggBlock;
import com.nyfaria.parkbuilder.entity.custom.*;
import com.nyfaria.parkbuilder.entity.dinos.DinoBase;
import com.nyfaria.parkbuilder.item.DNAItem;
import com.nyfaria.parkbuilder.item.DinoEggItem;
import com.nyfaria.parkbuilder.item.HatchedEggItem;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class EntityInit {
    public static void init(){
        FabricDefaultAttributeRegistry.register(EntityInit.TRICERATOPS, TriceratopsEntity.attributes());
        FabricDefaultAttributeRegistry.register(EntityInit.VELOCIRAPTORM, VelociraptorMEntity.attributes());
        FabricDefaultAttributeRegistry.register(EntityInit.VELOCIRAPTORA, VelociraptorAEntity.attributes());
        FabricDefaultAttributeRegistry.register(EntityInit.DRYOSAURUS, DryosaurusEntity.attributes());
        FabricDefaultAttributeRegistry.register(EntityInit.BARYONYX, BaryonyxEntity.attributes());
    }

    public static final EntityType<TriceratopsEntity> TRICERATOPS =
            registerEntity("triceratops",
                    EntityType.Builder.of(TriceratopsEntity::new, MobCategory.CREATURE)
                            .sized(3f, 3f));
    public static final EntityType<VelociraptorMEntity> VELOCIRAPTORM =
            registerEntity("velociraptor_mongolienisis",
                    EntityType.Builder.of(VelociraptorMEntity::new, MobCategory.MONSTER)
                            .sized(1.5f, 1.5f));
    public static final EntityType<VelociraptorAEntity> VELOCIRAPTORA =
            registerEntity("velociraptor_antirrhopus",
                    EntityType.Builder.of(VelociraptorAEntity::new, MobCategory.MONSTER)
                            .sized(1.5f, 2.0f));
    public static final EntityType<DryosaurusEntity> DRYOSAURUS =
            registerEntity("dryosaurus",
                    EntityType.Builder.of(DryosaurusEntity::new, MobCategory.CREATURE)
                            .sized(1.5f, 2.0f));
    public static final EntityType<BaryonyxEntity> BARYONYX =
            registerEntity("baryonyx",
                    EntityType.Builder.of((EntityType<BaryonyxEntity> entityType, Level level) -> new BaryonyxEntity(entityType, level), MobCategory.MONSTER)
                            .sized(2.0f, 2.0f));

    protected static<T extends DinoBase> EntityType<T> registerEntity(String name, EntityType.Builder<T> supplier) {
        EntityType<T> ENTITY = Registry.register(Registry.ENTITY_TYPE,ParkBuilder.modLoc(name), supplier.build(ParkBuilder.MOD_ID + ":" + name));
        Item HATCHED = Registry.register(Registry.ITEM,ParkBuilder.modLoc("hatched_" + name + "_egg"), new HatchedEggItem<>(ENTITY,ItemInit.getItemProperties().stacksTo(1)));
        Block EGG_BLOCK = Registry.register(Registry.BLOCK,ParkBuilder.modLoc(name + "_egg"), new DinoEggBlock(()->ENTITY,BlockBehaviour.Properties.of(Material.EGG).randomTicks().strength(0.5f, 0.5f)));
        Item EGG = Registry.register(Registry.ITEM,ParkBuilder.modLoc(name + "_egg"),  new DinoEggItem(HATCHED, EGG_BLOCK,ItemInit.getItemProperties().stacksTo(1)));
        Registry.register(Registry.ITEM,ParkBuilder.modLoc("dna_" + name),  new DNAItem(EGG,ItemInit.getItemProperties().stacksTo(1)));
        return ENTITY;
    }
}
