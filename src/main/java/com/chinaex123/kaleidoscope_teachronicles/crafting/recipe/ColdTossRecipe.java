package com.chinaex123.kaleidoscope_teachronicles.crafting.recipe;

import com.chinaex123.kaleidoscope_teachronicles.init.KTCRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * 冷炒配方。
 * <p>
 * 炒锅上的多份原料在冷态下翻炒转化为结果。
 * 匹配逻辑：逐个 ingredient 从输入槽中消耗一个匹配物品，全部 ingredient 消耗成功即视为匹配。
 * 可选声明 {@code carrier} 表示烹饪完成后需要用什么容器右键取出成品；
 * 未声明（Ingredient.EMPTY）时表示直接右键即可取走。
 *
 * @param ingredients   所需原料列表（多个 Ingredient）
 * @param result        产出结果物品
 * @param stirFryCount  翻炒次数，每次翻炒减少 1，归零后出锅
 * @param carrier       可选的取成品容器 Ingredient，EMPTY 表示直接右键取
 */
public record ColdTossRecipe(List<Ingredient> ingredients, ItemStack result, int stirFryCount, Ingredient carrier)
        implements Recipe<SimpleInput> {

    /**
     * 兼容旧调用的紧凑构造：默认 {@code carrier = Ingredient.EMPTY}。
     */
    public ColdTossRecipe(List<Ingredient> ingredients, ItemStack result, int stirFryCount) {
        this(ingredients, result, stirFryCount, Ingredient.EMPTY);
    }

    /**
     * 检查输入是否匹配本配方。
     * <p>
     * 逐 ingredient 从输入物品中消耗一个匹配项，全部消耗成功返回 true。
     *
     * @param inv   炒锅输入容器（包装 9 槽 inputs）
     * @param level 世界实例
     * @return 全部原料匹配返回 true
     */
    @Override
    public boolean matches(SimpleInput inv, Level level) {
        List<ItemStack> remaining = new ArrayList<>();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                remaining.add(stack.copy());
            }
        }
        for (Ingredient ingredient : ingredients) {
            boolean matched = false;
            for (ItemStack stack : remaining) {
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    stack.shrink(1);
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }
        return true;
    }

    /**
     * 组装配方输出。
     *
     * @param inv        炒锅输入容器
     * @param registries 注册表提供者
     * @return 产出物品堆
     */
    @Override
    public ItemStack assemble(SimpleInput inv, HolderLookup.Provider registries) {
        return result.copy();
    }

    /**
     * 判断配方是否可在指定尺寸的合成网格中制作。
     *
     * @param width  网格宽度
     * @param height 网格高度
     * @return 始终返回 true
     */
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    /**
     * 获取配方结果物品。
     *
     * @param registries 注册表提供者
     * @return 结果物品
     */
    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    /**
     * 获取配方序列化器。
     *
     * @return 冷炒配方序列化器
     */
    @Override
    public RecipeSerializer<?> getSerializer() {
        return KTCRecipes.COLD_TOSS_SERIALIZER.get();
    }

    /**
     * 获取配方类型。
     *
     * @return 冷炒配方类型
     */
    @Override
    public RecipeType<?> getType() {
        return KTCRecipes.COLD_TOSS_RECIPE;
    }
}