package com.chinaex123.kaleidoscope_teachronicles.compat.emi.category;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.crafting.recipe.ColdBrewTeapotRecipe;
import com.chinaex123.kaleidoscope_teachronicles.init.KTCRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

import java.util.Arrays;
import java.util.List;

/**
 * 冷萃配方的 EMI 展示。
 * <p>
 * 定义冷萃配方在 EMI 中的分类与展示布局，
 * 显示所需的茶液桶、原料与产出结果，并在底部居中显示处理时长。
 */
public class EmiColdBrewTeapotRecipe extends BasicEmiRecipe {

    /** EMI 配方分类，以茶壶物品作为分类图标 */
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            ResourceLocation.parse(KTCRecipes.COLD_BREW_TEAPOT_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModItems.TEAPOT.get()))
    );

    /** 界面背景纹理位置 */
    private static final ResourceLocation BG = KaleidoscopeTeaChronicles.id("textures/gui/jei/cold_brew_teapot.png");
    /** 界面宽度 */
    public static final int WIDTH = 176;
    /** 界面高度 */
    public static final int HEIGHT = 78;

    /** 展示所用的茶液桶物品 */
    private final Item fluidBucket;
    /** 处理时长（刻） */
    private final int brewTime;
    /** 容器 */
    private final EmiIngredient containerIngredient;

    /**
     * 构造冷萃配方的 EMI 展示条目。
     *
     * @param id          配方标识符
     * @param inputs      输入原料列表
     * @param outputs     产出结果列表
     * @param fluidBucket 展示所用的茶液桶物品
     * @param brewTime    处理时长（刻）
     */
    public EmiColdBrewTeapotRecipe(ResourceLocation id, List<EmiIngredient> inputs, List<EmiStack> outputs, Item fluidBucket, int brewTime, EmiIngredient containerIngredient) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = inputs;
        this.outputs = outputs;
        this.fluidBucket = fluidBucket;
        this.brewTime = brewTime;
        this.containerIngredient = containerIngredient;
    }

    /**
     * 向 EMI 注册分类、工作站与所有冷萃配方。
     * <p>
     * 遍历配方管理器中的冷萃配方，解析茶液桶、按数量准备输入原料，
     * 并按固定产出数量准备输出结果，逐条加入 EMI 注册表。
     *
     * @param registry EMI 注册表
     */
    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModItems.TEAPOT.get()));

        registry.getRecipeManager().getAllRecipesFor(KTCRecipes.COLD_BREW_TEAPOT_RECIPE).forEach(r -> {
            ColdBrewTeapotRecipe value = r.value();
            Fluid fluid = BuiltInRegistries.FLUID.get(value.teaFluid());
            Item bucket = fluid.getBucket();
            List<EmiIngredient> inputs = List.of(EmiIngredient.of(Arrays.stream(value.ingredient().getItems())
                    .map(stack -> EmiStack.of(stack.copyWithCount(value.ingredientCount())))
                    .toList()));
            List<EmiStack> outputs = List.of(EmiStack.of(value.result().copyWithCount(ColdBrewTeapotRecipe.OUTPUT_COUNT)));
            EmiIngredient containerEmi = EmiIngredient.of(Arrays.stream(value.container().getItems()).map(EmiStack::of).toList());
            registry.addRecipe(new EmiColdBrewTeapotRecipe(r.id(), inputs, outputs, bucket, value.time(), containerEmi));
        });
    }

    /**
     * 添加配方展示组件。
     * <p>
     * 绘制背景纹理，布置茶液桶、原料、仅渲染的杯子与产出槽位，
     * 并在底部居中显示处理时长（以秒为单位）。
     *
     * @param widgets 组件容器
     */
    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);

        widgets.addSlot(EmiStack.of(fluidBucket), 65, 0);
        widgets.addSlot(inputs.getFirst(), 83, 0);
        widgets.addSlot(containerIngredient, 122, 0)
                .drawBack(false);
        widgets.addSlot(outputs.getFirst(), 128, 45)
                .drawBack(false)
                .recipeContext(this);

        Component brewTimeText = Component.translatable("jei.kaleidoscope_teachronicles.cold_brew_teapot.time", brewTime / 20);
        int x = WIDTH / 2 - Minecraft.getInstance().font.width(brewTimeText) / 2;
        widgets.addText(brewTimeText, x, 70, 0x555555, false);
    }
}