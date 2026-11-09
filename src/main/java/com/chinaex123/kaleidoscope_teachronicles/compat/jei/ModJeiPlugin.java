package com.chinaex123.kaleidoscope_teachronicles.compat.jei;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.compat.jei.category.ColdBrewTeapotRecipeCategory;
import com.chinaex123.kaleidoscope_teachronicles.compat.jei.category.ColdStewRecipeCategory;
import com.chinaex123.kaleidoscope_teachronicles.compat.jei.category.ColdTossRecipeCategory;
import com.chinaex123.kaleidoscope_teachronicles.compat.jei.category.FermentingRecipeCategory;
import com.chinaex123.kaleidoscope_teachronicles.compat.jei.category.ScentingRecipeCategory;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

/**
 * 模组的 JEI 插件。
 * <p>
 * 负责向 JEI 注册本模组的配方分类、配方内容以及对应的配方催化剂，
 * 使冷萃茶、发酵与窨制三类配方可在 JEI 中查看。
 */
@JeiPlugin
public class ModJeiPlugin implements IModPlugin {

    /** JEI 插件唯一标识符 */
    private static final ResourceLocation UID = KaleidoscopeTeaChronicles.id("jei");

    /**
     * 注册配方分类。
     * <p>
     * 依次注册冷萃茶、发酵与窨制三类配方分类。
     *
     * @param registration 配方分类注册对象
     */
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ColdBrewTeapotRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FermentingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ScentingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ColdTossRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ColdStewRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    /**
     * 注册配方内容。
     * <p>
     * 将客户端当前可用的三类配方分别注册到对应的配方分类中。
     *
     * @param registration 配方注册对象
     */
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ColdBrewTeapotRecipeCategory.TYPE, ColdBrewTeapotRecipeCategory.getRecipes());
        registration.addRecipes(FermentingRecipeCategory.TYPE, FermentingRecipeCategory.getRecipes());
        registration.addRecipes(ScentingRecipeCategory.TYPE, ScentingRecipeCategory.getRecipes());
        registration.addRecipes(ColdTossRecipeCategory.TYPE, ColdTossRecipeCategory.getRecipes());
        registration.addRecipes(ColdStewRecipeCategory.TYPE, ColdStewRecipeCategory.getRecipes());
    }

    /**
     * 注册配方催化剂。
     * <p>
     * 将茶壶登记为冷萃茶配方的催化剂，
     * 将竹盘分别登记为发酵与窨制配方的催化剂。
     *
     * @param registration 配方催化剂注册对象
     */
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModItems.TEAPOT.get(), ColdBrewTeapotRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModBlocks.BAMBOO_TRAY.get(), FermentingRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModBlocks.BAMBOO_TRAY.get(), ScentingRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModBlocks.POT.get(), ColdTossRecipeCategory.TYPE);
        registration.addRecipeCatalyst(ModBlocks.STOCKPOT.get(), ColdStewRecipeCategory.TYPE);
    }

    /**
     * 获取 JEI 插件唯一标识符。
     *
     * @return 插件标识符
     */
    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }
}