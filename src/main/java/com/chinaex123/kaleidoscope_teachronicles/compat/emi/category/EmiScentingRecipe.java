package com.chinaex123.kaleidoscope_teachronicles.compat.emi.category;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.crafting.recipe.ScentingRecipe;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 窨制配方 EMI 展示。
 * <p>
 * 多输入到单输出，需等待一定时间。
 * 定义窨制配方在 EMI 中的分类与展示布局，
 * 按原料数量动态排列输入槽位，并在底部显示窨制时长。
 */
public class EmiScentingRecipe extends BasicEmiRecipe {

    /** EMI 配方分类，以竹盘方块作为分类图标 */
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            ResourceLocation.parse(KTCRecipes.SCENTING_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModBlocks.BAMBOO_TRAY.get()))
    );

    /** 背景纹理 */
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(
            KaleidoscopeTeaChronicles.MODID, "textures/gui/jei/scenting.png"
    );

    /** 界面宽度 */
    public static final int WIDTH = 176;
    /** 界面高度 */
    public static final int HEIGHT = 78;

    /** 窨制时长（刻） */
    private final int duration;

    /**
     * 构造窨制配方的 EMI 展示条目。
     *
     * @param id       配方标识符
     * @param inputs   输入原料列表
     * @param output   产出结果
     * @param duration 窨制时长（刻）
     */
    public EmiScentingRecipe(ResourceLocation id, List<EmiIngredient> inputs, EmiStack output, int duration) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = inputs;
        this.outputs = List.of(output);
        this.duration = duration;
    }

    /**
     * 向 EMI 注册分类、工作站与所有窨制配方。
     * <p>
     * 遍历配方管理器中的窨制配方，逐条构建输入原料列表与产出结果并加入 EMI 注册表。
     *
     * @param registry EMI 注册表
     */
    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModBlocks.BAMBOO_TRAY.get()));

        registry.getRecipeManager().getAllRecipesFor(KTCRecipes.SCENTING_RECIPE).forEach(r -> {
            ScentingRecipe value = r.value();
            List<EmiIngredient> inputs = new ArrayList<>();
            for (Ingredient ing : value.ingredients()) {
                inputs.add(EmiIngredient.of(ing));
            }
            EmiStack output = EmiStack.of(value.result());
            registry.addRecipe(new EmiScentingRecipe(r.id(), inputs, output, value.duration()));
        });
    }

    /**
     * 添加配方展示组件。
     * <p>
     * 按原料数量依次排列输入槽位，产出槽位置根据输入槽数量动态计算，
     * 并在底部左侧显示窨制时长（以秒为单位）。
     *
     * @param widgets 组件容器
     */
    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);

        int outputX = 131;
        int outputY = 29;
        widgets.addSlot(outputs.getFirst(), outputX, outputY).drawBack(false).recipeContext(this);

        int count = inputs.size();
        int centerX = 41;
        int spacing = 18 + 2;
        int inputY = 27;
        int[] offsets = {-spacing, 0, +spacing};
        int offsetIdx = (3 - count) / 2;

        for (int i = 0; i < count; i++) {
            int x = centerX + offsets[offsetIdx + i];
            widgets.addSlot(inputs.get(i), x, inputY).drawBack(false);
        }

        Component timeText = Component.translatable("jei.kaleidoscope_teachronicles.scenting.time", duration / 20);
        int x = 88 - Minecraft.getInstance().font.width(timeText) / 2;
        widgets.addText(timeText, x, 68, 0x555555, false);
    }
}