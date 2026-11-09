package com.chinaex123.kaleidoscope_teachronicles.data;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.init.KTCItems;
import com.chinaex123.kaleidoscope_teachronicles.init.KTCTeacups;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelsProvider extends ItemModelProvider {
    public ModItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, KaleidoscopeTeaChronicles.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // ==================== 厨具 ====================
        basicItem(KTCItems.CREAM_OIL.get());
        // ==================== 材料 ====================
        basicItem(KTCItems.MATCHA_POWDER.get());
        basicItem(KTCItems.CREAM.get());
        // ==================== 食物 ====================
        foodItem(KTCItems.MATCHA_BB.get());
        foodItem(KTCItems.MATCHA_PARFAIT.get());
        foodItem(KTCItems.MATCHA_CAKE_ROLL.get());
        foodItem(KTCItems.MATCHA_CAKE_SLICE.get());
        foodItem(KTCItems.MATCHA_CAKE.get());
        // ==================== 厨具 ====================
        basicItem(KTCItems.YUNOMI.get());
        // ==================== 茶叶 ====================
        basicItem(KTCItems.GREEN_TEA_LEAVES.get());
        // ==================== 混合茶叶 ====================
        // ==================== 茶包 ====================
        bagItem(KTCItems.ROSE_TEA_BAG.get());
        // ==================== 茶 ====================
        teaItem(TeacupRegistry.getItem(KTCTeacups.MATCHA_TEA));
        teaItem(TeacupRegistry.getItem(KTCTeacups.ROSE_TEA));
    }


    /**
     * 茶（路径：item/tea）
     */
    private void teaItem(Item item) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
        String itemName = key.getPath();
        ItemModelBuilder builder = withExistingParent(itemName, ResourceLocation.withDefaultNamespace("item/generated"));
        builder.texture("layer0", KaleidoscopeTeaChronicles.id("item/tea/" + itemName));
    }

    /**
     * 茶包（路径：item/teacup_bag/）
     */
    private void bagItem(Item item) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
        String itemName = key.getPath();
        ItemModelBuilder builder = withExistingParent(itemName, ResourceLocation.withDefaultNamespace("item/generated"));
        builder.texture("layer0", KaleidoscopeTeaChronicles.id("item/tea_bag/" + itemName));
    }
    /**
     * 食物（路径：item/food）
     */
    private void foodItem(Item item) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
        String itemName = key.getPath();
        ItemModelBuilder builder = withExistingParent(itemName, ResourceLocation.withDefaultNamespace("item/generated"));
        builder.texture("layer0", KaleidoscopeTeaChronicles.id("item/food/" + itemName));
    }
}
