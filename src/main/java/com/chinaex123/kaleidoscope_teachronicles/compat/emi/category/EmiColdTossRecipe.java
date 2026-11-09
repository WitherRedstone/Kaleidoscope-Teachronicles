package com.chinaex123.kaleidoscope_teachronicles.compat.emi.category;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.crafting.recipe.ColdTossRecipe;
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
 * 冷炒配方 EMI 展示。
 * <p>
 * 多输入到单输出，需等待一定时间。
 * 以原模组炒锅方块作为工作站与分类图标。
 */
public class EmiColdTossRecipe extends BasicEmiRecipe {

    /** EMI 配方分类，以炒锅方块作为分类图标 */
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
            ResourceLocation.parse(KTCRecipes.COLD_TOSS_RECIPE.toString()),
            EmiIngredient.of(Ingredient.of(ModBlocks.POT.get()))
    );

    /** 界面宽度 */
    public static final int WIDTH = 176;
    /** 界面高度 */
    public static final int HEIGHT = 102;

    /** 背景纹理 */
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(
            KaleidoscopeTeaChronicles.MODID, "textures/gui/jei/cold_toss_pot.png"
    );

    /** 冷炒所需翻炒次数 */
    private final int stirFryCount;
    /** 可选的取成品容器，null 表示配方未声明（直接右键取） */
    private final EmiIngredient carrier;

    /**
     * 构造冷炒配方的 EMI 展示条目。
     *
     * @param id       配方标识符
     * @param inputs   输入原料列表
     * @param output   产出结果
     * @param stirFryCount 冷炒所需翻炒次数
     * @param carrier  可选的取成品容器，null 表示配方未声明
     */
    public EmiColdTossRecipe(ResourceLocation id, List<EmiIngredient> inputs, EmiStack output, int stirFryCount, EmiIngredient carrier) {
        super(CATEGORY, id, WIDTH, HEIGHT);
        this.inputs = inputs;
        this.outputs = List.of(output);
        this.stirFryCount = stirFryCount;
        this.carrier = carrier;
    }

    /**
     * 向 EMI 注册分类、工作站与所有冷炒配方。
     * <p>
     * 遍历配方管理器中的冷炒配方，逐条构建输入原料列表、产出结果与可选取成品容器，
     * 并加入 EMI 注册表。
     *
     * @param registry EMI 注册表
     */
    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(ModBlocks.POT.get()));

        registry.getRecipeManager().getAllRecipesFor(KTCRecipes.COLD_TOSS_RECIPE).forEach(r -> {
            ColdTossRecipe value = r.value();
            List<EmiIngredient> inputs = new ArrayList<>();
            for (Ingredient ing : value.ingredients()) {
                inputs.add(EmiIngredient.of(ing));
            }
            EmiStack output = EmiStack.of(value.result());
            EmiIngredient carrierIng = value.carrier().isEmpty() ? null : EmiIngredient.of(value.carrier());
            registry.addRecipe(new EmiColdTossRecipe(r.id(), inputs, output, value.stirFryCount(), carrierIng));
        });
    }

    /**
     * 添加配方展示组件。
     * <p>
     * 绘制背景纹理与顶部说明文本，按每行三个的网格排列输入槽位；
     * 若存在取成品容器则额外放置一个槽位，随后放置产出槽位，
     * 并在底部居中显示冷炒时长（以秒为单位）。
     *
     * @param widgets 组件容器
     */
    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BG, 1, 1, WIDTH, HEIGHT, 0, 0);

        widgets.addText(
                Component.translatable("jei.kaleidoscope_cookery.strict_recipe"),
                WIDTH / 2, 5, 0x555555, false
        ).horizontalAlign(TextWidget.Alignment.CENTER);

        for (int i = 0; i < inputs.size(); i++) {
            int xOffset = (i % 3) * 18 + 15;
            int yOffset = (i / 3) * 18 + 24;
            widgets.addSlot(inputs.get(i), xOffset, yOffset).drawBack(false);
        }

        if (carrier != null) {
            widgets.addSlot(carrier, 133, 18).drawBack(false);
        }
        widgets.addSlot(outputs.getFirst(), 143, 60).drawBack(false).recipeContext(this);

        widgets.addText(
                Component.translatable("jei.kaleidoscope_teachronicles.cold_toss.time", stirFryCount),
                WIDTH / 2, 85, 0x555555, false
        ).horizontalAlign(TextWidget.Alignment.CENTER);
    }
}