package com.nyfaria.parkbuilder.Initialize;

import com.nyfaria.parkbuilder.ParkBuilder;
import com.nyfaria.parkbuilder.block.CultivatorBlock;
import com.nyfaria.parkbuilder.block.ExtractorBlock;
import com.nyfaria.parkbuilder.block.IncubatorBlock;
import com.nyfaria.parkbuilder.block.entity.cultivator.CultivatorBlockEntity;
import com.nyfaria.parkbuilder.block.entity.extractor.ExtractorBlockEntity;
import com.nyfaria.parkbuilder.block.entity.incubator.IncubatorBlockEntity;
import com.nyfaria.parkbuilder.item.BottomTopBlockItem;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockInit {

    public static void init(){
        Registry.register(Registry.BLOCK_ENTITY_TYPE,ParkBuilder.modLoc("extractor_block_entity"),BlockInit.EXTRACTOR_BLOCK_ENTITY);
        Registry.register(Registry.BLOCK_ENTITY_TYPE,ParkBuilder.modLoc("cultivator_block_entity"),BlockInit.CULTIVATOR_BLOCK_ENTITY);
        Registry.register(Registry.BLOCK_ENTITY_TYPE,ParkBuilder.modLoc("incubator_block_entity"),BlockInit.INCUBATOR_BLOCK_ENTITY);
    }
    public static final Block AMBER_ORE_STONE = registerBlock("amber_ore",new Block(BlockBehaviour.Properties.of(Material.STONE).noOcclusion().friction(0.5f).destroyTime(02f)));
    public static final Block AMBER_ORE_DEEPSLATE = registerBlock("deepslate_amber_ore",new Block(BlockBehaviour.Properties.of(Material.STONE).noOcclusion().friction(.5f).destroyTime(5f)));
    public static final Block FOSSIL_ORE_STONE = registerBlock("fossil_ore",new Block(BlockBehaviour.Properties.of(Material.STONE).noOcclusion().friction(0.5f).strength(3.0F, 3.0F)));
    public static final Block FOSSIL_ORE_DEEPSLATE = registerBlock("deepslate_fossil_ore",new Block(BlockBehaviour.Properties.of(Material.STONE).noOcclusion().friction(.5f).strength(4.5F, 3.0F)));
    public static final Block EXTRACTOR = registerBlock("extractor",new ExtractorBlock(BlockBehaviour.Properties.of(Material.STONE).noOcclusion().friction(.5f).strength(3.0F, 3.0F)));
    public static final Block CULTIVATOR = registerBottomTopBlock("cultivator",new CultivatorBlock(BlockBehaviour.Properties.of(Material.STONE).noOcclusion().friction(.5f).strength(3.0F, 3.0F)));
    public static final Block INCUBATOR = registerBottomTopBlock("incubator",new IncubatorBlock(BlockBehaviour.Properties.of(Material.STONE).noOcclusion().friction(.5f).strength(3.0F, 3.0F)));

    public static final BlockEntityType<ExtractorBlockEntity> EXTRACTOR_BLOCK_ENTITY =  FabricBlockEntityTypeBuilder.create(ExtractorBlockEntity::new, EXTRACTOR).build(null);
    public static final BlockEntityType<CultivatorBlockEntity> CULTIVATOR_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(CultivatorBlockEntity::new, CULTIVATOR).build(null);
    public static final BlockEntityType<IncubatorBlockEntity> INCUBATOR_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(IncubatorBlockEntity::new, INCUBATOR).build(null);


    protected static<T extends Block> T registerBlock(String name, T block) {
        return registerBlock(name, block, b ->new BlockItem(b, ItemInit.getItemProperties()));
    }
    protected static<T extends Block> T registerBottomTopBlock(String name, T block) {
        return registerBlock(name, block, b ->new BottomTopBlockItem(b, ItemInit.getItemProperties()));
    }

    protected static<T extends Block> T registerBlock(String name, T block, Function<Block, ? extends BlockItem> item) {
        T tblock = Registry.register(Registry.BLOCK,new ResourceLocation(ParkBuilder.MOD_ID,name), block);
        Registry.register(Registry.ITEM,new ResourceLocation(ParkBuilder.MOD_ID,name), item.apply(block));
        return tblock;
    }

}
