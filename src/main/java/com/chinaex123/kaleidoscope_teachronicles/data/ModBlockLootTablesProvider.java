package com.chinaex123.kaleidoscope_teachronicles.data;

import com.chinaex123.kaleidoscope_teachronicles.init.KTCBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ModBlockLootTablesProvider extends BlockLootSubProvider {
    public ModBlockLootTablesProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(KTCBlocks.COLD_STOVE.get());
        dropSelf(KTCBlocks.CREAM_OIL.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return List.of(
                KTCBlocks.COLD_STOVE.get(),
                KTCBlocks.CREAM_OIL.get()
        );
    }
}
