package com.chinaex123.kaleidoscope_teachronicles.compat.emi;

import com.chinaex123.kaleidoscope_teachronicles.compat.emi.category.EmiColdBrewTeapotRecipe;
import com.chinaex123.kaleidoscope_teachronicles.compat.emi.category.EmiColdStewRecipe;
import com.chinaex123.kaleidoscope_teachronicles.compat.emi.category.EmiColdTossRecipe;
import com.chinaex123.kaleidoscope_teachronicles.compat.emi.category.EmiFermentingRecipe;
import com.chinaex123.kaleidoscope_teachronicles.compat.emi.category.EmiScentingRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;

/**
 * 模组的 EMI 插件。
 * <p>
 * 作为 EMI 的入口点，负责在 EMI 初始化时注册本模组的配方分类与配方内容，
 * 使冷萃茶、发酵与窨制三类配方可在 EMI 中查看。
 */
@EmiEntrypoint
public class ModEmiPlugin implements EmiPlugin {

    /**
     * 向 EMI 注册配方。
     * <p>
     * 依次调用三类配方的注册方法，完成分类、工作站与配方内容的注册。
     *
     * @param registry EMI 注册表
     */
    @Override
    public void register(EmiRegistry registry) {
        EmiColdBrewTeapotRecipe.register(registry);
        EmiFermentingRecipe.register(registry);
        EmiScentingRecipe.register(registry);
        EmiColdTossRecipe.register(registry);
        EmiColdStewRecipe.register(registry);
    }
}