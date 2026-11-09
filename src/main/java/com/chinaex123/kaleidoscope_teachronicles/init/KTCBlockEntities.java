package com.chinaex123.kaleidoscope_teachronicles.init;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.blockentity.CreamOilBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KTCBlockEntities {
    DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE, KaleidoscopeTeaChronicles.MODID);

    /** 奶油油壶 BlockEntity，只绑定 CreamOilBlock */
    DeferredHolder<BlockEntityType<?>, BlockEntityType<CreamOilBlockEntity>> CREAM_OIL =
            BLOCK_ENTITIES.register("cream_oil",
                    () -> BlockEntityType.Builder.of(
                            CreamOilBlockEntity::new,
                            KTCBlocks.CREAM_OIL.get()
                    ).build(null));
}