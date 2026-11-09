package com.chinaex123.kaleidoscope_teachronicles.init;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.block.ColdStoveBlock;
import com.chinaex123.kaleidoscope_teachronicles.block.CreamOilBlock;
import com.chinaex123.kaleidoscope_teachronicles.block.YunomiBlock;
import com.chinaex123.kaleidoscope_teachronicles.block.cake.MatchaCake;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KTCBlocks {
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(KaleidoscopeTeaChronicles.MODID);

    /** 冷灶 */
    DeferredBlock<Block> COLD_STOVE = BLOCKS.register("cold_stove", ColdStoveBlock::new);
    /** 奶油油壶 */
    DeferredBlock<Block> CREAM_OIL = BLOCKS.register("cream_oil", CreamOilBlock::new);
    /** 汤吞 */
    DeferredBlock<Block> YUNOMI = BLOCKS.register("yunomi", YunomiBlock::new);
    /** 抹茶蛋糕 */
    DeferredBlock<Block> MATCHA_CAKE = BLOCKS.register("matcha_cake", MatchaCake::new);
}