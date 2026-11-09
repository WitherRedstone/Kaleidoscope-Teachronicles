package com.chinaex123.kaleidoscope_teachronicles.compat.emi.category;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.crafting.recipe.ColdStewRecipe;
import com.chinaex123.kaleidoscope_teachronicles.init.KTCRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.api.widget.TextWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * 冷炖配方 EMI 展示。
 * <p>
 * 多原料 + 汤底到单输出，需等待一定时间。
 * 以汤锅方块作为工作站/分类图标。
 */
public class EmiColdStewRecipe extends BasicEmiRecipe {

    /** EMI 配方分类，以汤锅方块作为分类图标 */
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            ResourceLocation.parse(KTCRecipes.COLD_STEW_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModBlocks.STOCKPOT.get()))
    );

    /** 界面宽度 */
    public static final int WIDTH = 176;
    /** 界面高度 */
    public static final int HEIGHT = 102;

    /** 背景纹理 */
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(
            KaleidoscopeTeaChronicles.MODID, "textures/gui/jei/cold_stew.png"
    );

    /** 冷炖时长（刻） */
    private final int duration;

    public EmiColdStewRecipe(ResourceLocation id, List<EmiIngredient> inputs, EmiStack output, int duration) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = inputs;
        this.outputs = List.of(output);
        this.duration = duration;
    }

    /**
     * 向 EMI 注册分类、工作站与所有冷炖配方。
     *
     * @param registry EMI 注册表
     */
    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModBlocks.STOCKPOT.get()));

        registry.getRecipeManager().getAllRecipesFor(KTCRecipes.COLD_STEW_RECIPE).forEach(r -> {
            ColdStewRecipe value = r.value();
            List<EmiIngredient> inputs = new ArrayList<>();

            for (Ingredient ing : value.ingredients()) {
                inputs.add(EmiIngredient.of(ing));
            }

            if (!value.soupBase().isEmpty()) {
                inputs.add(EmiIngredient.of(value.soupBase()));
            }
            EmiStack output = EmiStack.of(value.result());
            registry.addRecipe(new EmiColdStewRecipe(r.id(), inputs, output, value.duration()));
        });
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);

        widgets.addText(
                Component.translatable("jei.kaleidoscope_cookery.strict_recipe"),
                WIDTH / 2, 5, 0x555555, false
        ).horizontalAlign(TextWidget.Alignment.CENTER);

        int ingredientCount = inputs.size() - 1;
        for (int i = 0; i < ingredientCount; i++) {
            int xOffset = (i % 3) * 18 + 15;
            int yOffset = (i / 3) * 18 + 24;
            widgets.addSlot(inputs.get(i), xOffset, yOffset).drawBack(false);
        }

        if (!inputs.isEmpty()) {
            widgets.addSlot(inputs.getLast(), 133, 18).drawBack(false);
        }

        widgets.addSlot(outputs.getFirst(), 143, 60).drawBack(false).recipeContext(this);
        widgets.addText(
                Component.translatable("jei.kaleidoscope_teachronicles.cold_stew.time", duration / 20),
                WIDTH / 2, 85, 0x555555, false
        ).horizontalAlign(TextWidget.Alignment.CENTER);
    }
}