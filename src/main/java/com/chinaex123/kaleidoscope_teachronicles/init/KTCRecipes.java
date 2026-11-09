package com.chinaex123.kaleidoscope_teachronicles.init;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.crafting.recipe.ColdBrewTeapotRecipe;
import com.chinaex123.kaleidoscope_teachronicles.crafting.recipe.ColdStewRecipe;
import com.chinaex123.kaleidoscope_teachronicles.crafting.recipe.ColdTossRecipe;
import com.chinaex123.kaleidoscope_teachronicles.crafting.recipe.FermentingRecipe;
import com.chinaex123.kaleidoscope_teachronicles.crafting.recipe.ScentingRecipe;
import com.chinaex123.kaleidoscope_teachronicles.crafting.serializer.ColdBrewTeapotRecipeSerializer;
import com.chinaex123.kaleidoscope_teachronicles.crafting.serializer.ColdStewRecipeSerializer;
import com.chinaex123.kaleidoscope_teachronicles.crafting.serializer.ColdTossRecipeSerializer;
import com.chinaex123.kaleidoscope_teachronicles.crafting.serializer.FermentingRecipeSerializer;
import com.chinaex123.kaleidoscope_teachronicles.crafting.serializer.ScentingRecipeSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber
public class KTCRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, KaleidoscopeTeaChronicles.MODID);

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> COLD_BREW_TEAPOT_SERIALIZER =
            RECIPE_SERIALIZERS.register("cold_brew_teapot", ColdBrewTeapotRecipeSerializer::new);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> FERMENTING_SERIALIZER =
            RECIPE_SERIALIZERS.register("fermenting", FermentingRecipeSerializer::new);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> SCENTING_SERIALIZER =
            RECIPE_SERIALIZERS.register("scenting", ScentingRecipeSerializer::new);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> COLD_TOSS_SERIALIZER =
            RECIPE_SERIALIZERS.register("cold_toss", ColdTossRecipeSerializer::new);
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> COLD_STEW_SERIALIZER =
            RECIPE_SERIALIZERS.register("cold_stew", ColdStewRecipeSerializer::new);

    /** 冷萃 */
    public static RecipeType<ColdBrewTeapotRecipe> COLD_BREW_TEAPOT_RECIPE;
    /** 发酵 */
    public static RecipeType<FermentingRecipe> FERMENTING_RECIPE;
    /** 窨制 */
    public static RecipeType<ScentingRecipe> SCENTING_RECIPE;
    /** 冷炒 */
    public static RecipeType<ColdTossRecipe> COLD_TOSS_RECIPE;
    /** 冷炖 */
    public static RecipeType<ColdStewRecipe> COLD_STEW_RECIPE;

    @SubscribeEvent
    public static void register(RegisterEvent evt) {
        if (evt.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
            COLD_BREW_TEAPOT_RECIPE = RecipeType.simple(KaleidoscopeTeaChronicles.id("cold_brew_teapot"));
            FERMENTING_RECIPE = RecipeType.simple(KaleidoscopeTeaChronicles.id("fermenting"));
            SCENTING_RECIPE = RecipeType.simple(KaleidoscopeTeaChronicles.id("scenting"));
            COLD_TOSS_RECIPE = RecipeType.simple(KaleidoscopeTeaChronicles.id("cold_toss"));
            COLD_STEW_RECIPE = RecipeType.simple(KaleidoscopeTeaChronicles.id("cold_stew"));
        }
    }
}