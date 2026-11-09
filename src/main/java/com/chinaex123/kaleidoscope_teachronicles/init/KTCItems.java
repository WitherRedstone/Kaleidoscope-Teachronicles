package com.chinaex123.kaleidoscope_teachronicles.init;

import com.chinaex123.kaleidoscope_teachronicles.KaleidoscopeTeaChronicles;
import com.chinaex123.kaleidoscope_teachronicles.item.CreamItem;
import com.chinaex123.kaleidoscope_teachronicles.item.CreamOilBlockItem;
import com.chinaex123.kaleidoscope_teachronicles.item.YunomiItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface KTCItems {
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeTeaChronicles.MODID);

    // ==================== 厨具 ====================
    /** 冷灶 */
    DeferredItem<Item> COLD_STOVE = ITEMS.register("cold_stove", () -> new BlockItem(KTCBlocks.COLD_STOVE.get(), new Item.Properties()));
    /** 云吞 */
    DeferredItem<Item> YUNOMI = ITEMS.register("yunomi", YunomiItem::new);
    /** 奶油油壶 */
    DeferredItem<BlockItem> CREAM_OIL = ITEMS.register("cream_oil", () -> new CreamOilBlockItem(KTCBlocks.CREAM_OIL.get(), new Item.Properties().stacksTo(1)));

    // ==================== 材料 ====================
    /** 抹茶粉 */
    DeferredItem<Item> MATCHA_POWDER = ITEMS.register("matcha_powder", () -> new Item(new Item.Properties()));
    /** 奶油 */
    DeferredItem<Item> CREAM = ITEMS.register("cream", () -> new CreamItem(new Item.Properties()));

    // ==================== 食物 ====================
    /** 抹茶棒冰 */
    DeferredItem<Item> MATCHA_BB = ITEMS.register("matcha_bb", () -> new Item(new Item.Properties().food(KTCFoods.POPSICLE)));
    /** 抹茶芭菲 */
    DeferredItem<Item> MATCHA_PARFAIT = ITEMS.register("matcha_parfait", () -> new Item(new Item.Properties().food(KTCFoods.PARFAIT)));
    /** 抹茶蛋糕卷 */
    DeferredItem<Item> MATCHA_CAKE_ROLL = ITEMS.register("matcha_cake_roll", () -> new Item(new Item.Properties().food(KTCFoods.CAKE_ROLL)));
    /** 抹茶蛋糕切片 */
    DeferredItem<Item> MATCHA_CAKE_SLICE = ITEMS.register("matcha_cake_slice", () -> new Item(new Item.Properties().food(KTCFoods.CAKE_SLICE)));
    /** 抹茶蛋糕 */
    DeferredItem<BlockItem> MATCHA_CAKE = ITEMS.register("matcha_cake", () -> new BlockItem(KTCBlocks.MATCHA_CAKE.get(), new Item.Properties().stacksTo(1)));

    // ==================== 茶叶 ====================
    /** 绿茶茶叶 */
    DeferredItem<Item> GREEN_TEA_LEAVES = ITEMS.register("green_tea_leaves", () -> new Item(new Item.Properties()));

    // ==================== 混合茶叶 ====================

//    // ==================== 茶包 ====================
    /** 玫瑰花茶茶包 */
    DeferredItem<Item> ROSE_TEA_BAG = ITEMS.register("rose_tea_bag", () -> new Item(new Item.Properties()));

}