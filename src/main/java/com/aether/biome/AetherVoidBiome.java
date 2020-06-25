package com.aether.biome;

import com.aether.block.AetherBlocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.DefaultSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class AetherVoidBiome extends Biome {

    public AetherVoidBiome() {
        super((new Biome.Builder())
                .surfaceBuilder(new DefaultSurfaceBuilder(SurfaceBuilderConfig::deserialize), new SurfaceBuilderConfig(AetherBlocks.AETHER_GRASS_BLOCK.getDefaultState(), AetherBlocks.AETHER_DIRT.getDefaultState(), AetherBlocks.AETHER_DIRT.getDefaultState()))
                .precipitation(RainType.RAIN)
                .category(Category.NONE)
                .depth(0.125F)
                .scale(0.05F)
                .temperature(0.8F)
                .downfall(0.4F)
                .waterColor(4159204)
                .waterFogColor(329011)
                .parent((null))
                );
    }

}
