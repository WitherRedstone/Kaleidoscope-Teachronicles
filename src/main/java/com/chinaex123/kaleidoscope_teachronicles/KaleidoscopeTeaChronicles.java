package com.chinaex123.kaleidoscope_teachronicles;

import com.chinaex123.kaleidoscope_teachronicles.crafting.soupbase.PowderSnowSoupBase;
import com.chinaex123.kaleidoscope_teachronicles.init.*;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;

@Mod(KaleidoscopeTeaChronicles.MODID)
public class KaleidoscopeTeaChronicles {
    public static final String MODID = "kaleidoscope_teachronicles";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KaleidoscopeTeaChronicles(IEventBus modEventBus, ModContainer modContainer) {
        KTCBlocks.BLOCKS.register(modEventBus);
        KTCBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        KTCItems.ITEMS.register(modEventBus);
        KTCEffects.EFFECTS.register(modEventBus);
        KTCRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        KTCTeacups.init();
        KTCCreativeTabs.register(modEventBus);

        SoupBaseManager.registerSoupBase(PowderSnowSoupBase.INSTANCE);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(KaleidoscopeTeaChronicles.MODID, name);
    }
}