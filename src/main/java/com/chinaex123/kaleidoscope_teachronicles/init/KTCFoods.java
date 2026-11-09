package com.chinaex123.kaleidoscope_teachronicles.init;

import net.minecraft.world.food.FoodProperties;

public interface KTCFoods {

    /* 棒冰 */
    FoodProperties POPSICLE = new FoodProperties.Builder().nutrition(3).saturationModifier(0.4f).build();
    /* 巴菲 */
    FoodProperties PARFAIT = new FoodProperties.Builder().nutrition(8).saturationModifier(0.5f).build();
    /* 蛋糕卷 */
    FoodProperties CAKE_ROLL = new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).build();
    /* 蛋糕片 */
    FoodProperties CAKE_SLICE = new FoodProperties.Builder().nutrition(8).saturationModifier(0.4f).build();
    /* 蛋糕 */
    FoodProperties CAKE = new FoodProperties.Builder().nutrition(56).saturationModifier(2.8f).build();
}
