package com.chinaex123.kaleidoscope_teachronicles.data.recipe;

import com.chinaex123.kaleidoscope_teachronicles.data.builder.*;
import com.chinaex123.kaleidoscope_teachronicles.init.KTCItems;
import com.chinaex123.kaleidoscope_teachronicles.init.KTCTeacups;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.ModRecipeProvider;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;

import java.util.concurrent.CompletableFuture;

public class TeapotRecipeProvider extends ModRecipeProvider {
    public TeapotRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        buildColdBrewTeapotRecipes(output);
        buildFermentingRecipes(output);
        buildScentingRecipes(output);
        buildColdTossRecipes(output);
        buildColdStewRecipes(output);
    }

    /** 冷萃配方 */
    private void buildColdBrewTeapotRecipes(RecipeOutput output) {

        ColdBrewTeapotRecipeBuilder.builder()
                .setTeaFluid(Fluids.WATER)
                .setIngredient(Ingredient.of(KTCItems.MATCHA_POWDER))
                .setIngredientCount(1)
                .setResult(new ItemStack(TeacupRegistry.getItem(KTCTeacups.MATCHA_TEA)))
                .setContainer(Ingredient.of(KTCItems.YUNOMI))
                .setTime(240)
                .save(output);

    }

    /** 发酵配方 */
    private void buildFermentingRecipes(RecipeOutput output) {

        FermentingRecipeBuilder.builder()
                .setIngredient(Ingredient.of(Items.DIAMOND))
                .setResult(new ItemStack(Items.EMERALD))
                .setDuration(1200)
                .setMinLight(10)
                .setMaxLight(12)
                .save(output);

    }

    /** 窨制配方 */
    private void buildScentingRecipes(RecipeOutput output) {

        ScentingRecipeBuilder.builder()
                .setIngredients(
                        Items.EMERALD,
                        Items.EMERALD,
                        Items.EMERALD
                )
                .setResult(new ItemStack(Items.DIAMOND))
                .setDuration(1200)
                .save(output);

    }

    /** 冷炒配方 */
    private void buildColdTossRecipes(RecipeOutput output) {

        // 铁锭 → 铁块
        ColdTossRecipeBuilder.builder()
                .addIngredient(Ingredient.of(Items.IRON_INGOT))
                .setCarrier(Ingredient.of(Items.BOWL))
                .setResult(new ItemStack(Items.IRON_BLOCK))
                .setStirFryCount(5)
                .save(output);
        // 钻石 → 钻石块
        ColdTossRecipeBuilder.builder()
                .addIngredient(Ingredient.of(Items.DIAMOND))
                .setResult(new ItemStack(Items.DIAMOND_BLOCK))
                .setStirFryCount(5)
                .save(output);

    }

    /** 冷炖配方 */
    private void buildColdStewRecipes(RecipeOutput output) {

        // 牛肉 → 熟牛肉
        ColdStewRecipeBuilder.builder()
                .setSoupBase(Items.MILK_BUCKET)
                .addIngredient(Ingredient.of(Items.BEEF))
                .setResult(new ItemStack(Items.COOKED_BEEF))
                .setDuration(600)
                .save(output);

        ColdStewRecipeBuilder.builder()
                .setSoupBase(Items.POWDER_SNOW_BUCKET)
                .addIngredient(Ingredient.of(Items.DIAMOND))
                .addIngredient(Ingredient.of(KTCItems.MATCHA_POWDER))
                .setResult(new ItemStack(Items.MUSHROOM_STEW))
                .setDuration(800)
                .save(output);

//        ColdStewRecipeBuilder.builder()
//                .addIngredient(Ingredient.of(Items.POTATO))
//                .addIngredient(Ingredient.of(Items.CARROT))
//                .setResult(new ItemStack(Items.SUSPICIOUS_STEW))
//                .setDuration(1200)
//                .save(output);
//
//        ColdStewRecipeBuilder.builder()
//                .setSoupBase(Items.WATER_BUCKET, Items.GLASS_BOTTLE)
//                .addIngredient(Ingredient.of(Items.DANDELION))
//                .setResult(new ItemStack(Items.SUSPICIOUS_STEW))
//                .setDuration(1200)
//                .save(output);
//
//        ColdStewRecipeBuilder.builder()
//                .setSoupBase(Items.WATER_BUCKET)
//                .addIngredient(Ingredient.of(Items.POTATO))
//                .addIngredient(Ingredient.of(Items.CARROT))
//                .setResult(new ItemStack(Items.SUSPICIOUS_STEW))
//                .setDuration(1200)
//                .save(output);

    }
}