package com.chinaex123.kaleidoscope_teachronicles.init;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.effect.AftertasteEffect;
import com.chinaex123.kaleidoscope_teachronicles.effect.InvigoratingEffect;
import com.chinaex123.kaleidoscope_teachronicles.effect.LingeringEffect;
import com.chinaex123.kaleidoscope_teachronicles.effect.RefreshingEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KTCEffects {
    DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, KaleidoscopeTeaChronicles.MODID);

    /** 清爽 */
    DeferredHolder<MobEffect, MobEffect> REFRESHING = EFFECTS.register("refreshing", () -> new RefreshingEffect(0x7EC8E3));
    /** 回甘 */
    DeferredHolder<MobEffect, MobEffect> AFTER_TASTE = EFFECTS.register("after_taste", () -> new AftertasteEffect(0x90EE90));
    /** 提神 */
    DeferredHolder<MobEffect, MobEffect> INVIGORATING = EFFECTS.register("invigorating", () -> new InvigoratingEffect(0xFFD700));
    /** 余韵 */
    DeferredHolder<MobEffect, MobEffect> LINGERING = EFFECTS.register("lingering", () -> new LingeringEffect(0xE6E6FA));
}
