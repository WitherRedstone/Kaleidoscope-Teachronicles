package com.chinaex123.kaleidoscope_teachronicles.init;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class KTCTeacups {

    /** 抹茶 */
    public static ResourceLocation MATCHA_TEA;
    /** 玫瑰花茶 */
    public static ResourceLocation ROSE_TEA;

    public static void init() {
        TeacupRegistry registry = new TeacupRegistry();

        MATCHA_TEA = registry.registerTeacupData(KaleidoscopeTeaChronicles.id("matcha_tea"),
                TeacupRegistry.TeacupData.create(4).addEffect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 2 * 60 * 20)));
        ROSE_TEA = registry.registerTeacupData(KaleidoscopeTeaChronicles.id("rose_tea"),
                TeacupRegistry.TeacupData.create(4).addEffect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 2 * 60 * 20)));
    }
}
