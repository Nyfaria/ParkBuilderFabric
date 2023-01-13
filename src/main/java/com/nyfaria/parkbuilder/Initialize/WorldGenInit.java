package com.nyfaria.parkbuilder.Initialize;

import com.nyfaria.parkbuilder.ParkBuilder;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class WorldGenInit {

    public static void init(){
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY,
                        ParkBuilder.modLoc("fossil_ore")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY,
                        ParkBuilder.modLoc("fossil_ore_deepslate")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY,
                        ParkBuilder.modLoc("amber_ore")));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES,
                ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY,
                        ParkBuilder.modLoc("amber_ore_deepslate")));
    }

    public static final ConfiguredFeature<?,?> CONFIGURED_FOSSIL_ORE = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ParkBuilder.modLoc("fossil_ore"),new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, BlockInit.FOSSIL_ORE_STONE.defaultBlockState())), 5)));
    public static final ConfiguredFeature<?,?> CONFIGURED_DEEPSLATE_FOSSIL_ORE = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ParkBuilder.modLoc("fossil_ore_deepslate"), new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockInit.FOSSIL_ORE_DEEPSLATE.defaultBlockState())), 5)));


    public static final PlacedFeature PLACED_FOSSIL_ORE = Registry.register(BuiltinRegistries.PLACED_FEATURE, ParkBuilder.modLoc("fossil_ore"), new PlacedFeature(Holder.direct(CONFIGURED_FOSSIL_ORE), commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(30)))));
    public static final PlacedFeature PLACED_DEEPSLATE_FOSSIL_ORE = Registry.register(BuiltinRegistries.PLACED_FEATURE, ParkBuilder.modLoc("fossil_ore_deepslate"), new PlacedFeature(Holder.direct(CONFIGURED_DEEPSLATE_FOSSIL_ORE), commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(8)))));


    public static final ConfiguredFeature<?,?> CONFIGURED_AMBER_ORE = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ParkBuilder.modLoc("amber_ore"), new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, BlockInit.AMBER_ORE_STONE.defaultBlockState())), 1)));
    public static final ConfiguredFeature<?,?> CONFIGURED_DEEPSLATE_AMBER_ORE = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ParkBuilder.modLoc("amber_ore_deepslate"), new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(List.of(OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockInit.AMBER_ORE_DEEPSLATE.defaultBlockState())), 1)));


    public static final PlacedFeature PLACED_AMBER_ORE = Registry.register(BuiltinRegistries.PLACED_FEATURE, ParkBuilder.modLoc("amber_ore"), new PlacedFeature(Holder.direct(CONFIGURED_AMBER_ORE), commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(30)))));
    public static final PlacedFeature PLACED_DEEPSLATE_AMBER_ORE = Registry.register(BuiltinRegistries.PLACED_FEATURE, ParkBuilder.modLoc("amber_ore_deepslate"), new PlacedFeature(Holder.direct(CONFIGURED_DEEPSLATE_AMBER_ORE), commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(8)))));



    protected static TreeConfiguration.TreeConfigurationBuilder createStraightBlobTree(Block p_195147_, Block p_195148_, int p_195149_, int p_195150_, int p_195151_, int p_195152_) {
        return new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(p_195147_), new StraightTrunkPlacer(p_195149_, p_195150_, p_195151_), BlockStateProvider.simple(p_195148_), new BlobFoliagePlacer(ConstantInt.of(p_195152_), ConstantInt.of(0), 3), new TwoLayersFeatureSize(1, 0, 1));
    }



    private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }
    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }


}
