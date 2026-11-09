package com.chinaex123.kaleidoscope_teachronicles.compat.emi.category;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.crafting.recipe.FermentingRecipe;
import com.chinaex123.kaleidoscope_teachronicles.init.KTCRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

/**
 * 发酵配方 EMI 展示。
 * <p>
 * 单输入到单输出，需光照在配方指定区间内。
 * 定义发酵配方在 EMI 中的分类与展示布局，
 * 显示原料与产出槽位，并在底部显示所需光照条件与发酵时长。
 */
public class EmiFermentingRecipe extends BasicEmiRecipe {

    /** EMI 配方分类，以竹盘方块作为分类图标 */
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            ResourceLocation.parse(KTCRecipes.FERMENTING_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModBlocks.BAMBOO_TRAY.get()))
    );

    /** 背景纹理 */
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(
            KaleidoscopeTeaChronicles.MODID, "textures/gui/jei/fermenting.png"
    );

    /** 界面宽度 */
    public static final int WIDTH = 176;
    /** 界面高度 */
    public static final int HEIGHT = 78;

    /** 发酵时长（刻） */
    private final int duration;
    /** 最小光照等级 */
    private final int minLight;
    /** 最大光照等级 */
    private final int maxLight;

    /**
     * 构造发酵配方的 EMI 展示条目。
     *
     * @param id       配方标识符
     * @param input    输入原料
     * @param output   产出结果
     * @param duration 发酵时长（刻）
     * @param minLight 最小光照等级
     * @param maxLight 最大光照等级
     */
    public EmiFermentingRecipe(ResourceLocation id, EmiIngredient input, EmiStack output, int duration, int minLight, int maxLight) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = List.of(input);
        this.outputs = List.of(output);
        this.duration = duration;
        this.minLight = minLight;
        this.maxLight = maxLight;
    }

    /**
     * 向 EMI 注册分类、工作站与所有发酵配方。
     * <p>
     * 遍历配方管理器中的发酵配方，逐条构建输入原料与产出结果并加入 EMI 注册表。
     *
     * @param registry EMI 注册表
     */
    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModBlocks.BAMBOO_TRAY.get()));

        registry.getRecipeManager().getAllRecipesFor(KTCRecipes.FERMENTING_RECIPE).forEach(r -> {
            FermentingRecipe value = r.value();
            EmiIngredient input = EmiIngredient.of(value.ingredient());
            EmiStack output = EmiStack.of(value.result());
            registry.addRecipe(new EmiFermentingRecipe(r.id(), input, output, value.duration(), value.minLight(), value.maxLight()));
        });
    }

    /**
     * 添加配方展示组件。
     * <p>
     * 布置左侧原料输入槽与右侧产出槽位，
     * 并在底部两侧分别显示所需光照条件与发酵时长（以秒为单位）。
     *
     * @param widgets 组件容器
     */
    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);
        widgets.addSlot(inputs.getFirst(), 41, 27).drawBack(false);
        widgets.addSlot(outputs.getFirst(), 131, 29).drawBack(false).recipeContext(this);
        Component infoText = Component.translatable("jei.kaleidoscope_teachronicles.fermenting.info",
                minLight, maxLight, duration / 20);
        int x = 88 - Minecraft.getInstance().font.width(infoText) / 2;
        widgets.addText(infoText, x, 68, 0x555555, false);
    }
}