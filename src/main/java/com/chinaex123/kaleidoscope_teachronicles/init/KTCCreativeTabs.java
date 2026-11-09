package com.chinaex123.kaleidoscope_teachronicles.init;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.block.CreamOilBlock;
import com.chinaex123.kaleidoscope_teachronicles.blockentity.CreamOilBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KTCCreativeTabs {
    DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaleidoscopeTeaChronicles.MODID);

    DeferredHolder<CreativeModeTab, CreativeModeTab> KALEIDOSCOPE_TEACHRONICLES =
            CREATIVE_MODE_TABS.register(KaleidoscopeTeaChronicles.MODID, () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(TeacupRegistry.getItem(KTCTeacups.MATCHA_TEA)))
                    .title(Component.translatable("itemGroup.kaleidoscope_teachronicles"))
                    .displayItems((itemDisplayParameters, output) -> {
                        /* 奶油油壶 */
                        ItemStack fullCreamOil = new ItemStack(KTCItems.CREAM_OIL.get());

                        // 厨具
                        output.accept(KTCItems.COLD_STOVE);
                        output.accept(KTCItems.YUNOMI);
                        output.accept(KTCItems.CREAM_OIL);
                        CreamOilBlock.writeCreamCount(fullCreamOil, CreamOilBlockEntity.MAX_CREAM_COUNT);
                        // 材料
                        output.accept(KTCItems.MATCHA_POWDER);
                        output.accept(KTCItems.CREAM);
                        // 食物
                        output.accept(KTCItems.MATCHA_BB);
                        output.accept(KTCItems.MATCHA_PARFAIT);
                        output.accept(KTCItems.MATCHA_CAKE_ROLL);
                        output.accept(KTCItems.MATCHA_CAKE_SLICE);
                        output.accept(KTCItems.MATCHA_CAKE);
                        // 茶叶
                        output.accept(KTCItems.GREEN_TEA_LEAVES);
                        // 混合茶叶
                        // 茶包
                        output.accept(KTCItems.ROSE_TEA_BAG);
                        // 新茶
                        output.accept(TeacupRegistry.getItem(KTCTeacups.MATCHA_TEA));
                        output.accept(TeacupRegistry.getItem(KTCTeacups.ROSE_TEA));
                    })
                    .build());

    static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}