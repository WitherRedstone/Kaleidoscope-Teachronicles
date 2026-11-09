package com.chinaex123.kaleidoscope_teachronicles.mixin;

import com.chinaex123.kaleidoscope_teachronicles.block.CreamOilBlock;
import com.chinaex123.kaleidoscope_teachronicles.blockentity.CreamOilBlockEntity;
import com.chinaex123.kaleidoscope_teachronicles.crafting.recipe.ColdTossRecipe;
import com.chinaex123.kaleidoscope_teachronicles.init.KTCBlocks;
import com.chinaex123.kaleidoscope_teachronicles.init.KTCItems;
import com.chinaex123.kaleidoscope_teachronicles.init.KTCRecipes;
import com.chinaex123.kaleidoscope_teachronicles.item.CarrierSetter;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.PotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Optional;

/**
 * 为炒锅添加冷炒配方处理。
 * <p>
 * 当炒锅下方为已点燃的冷灶（冷态）且锅内有原料时，
 * 使用冷炒配方替代原烹饪配方推进状态机；
 * 同时扩展了放置油脂的交互，支持奶油与奶油油壶。
 */
@Mixin(PotBlockEntity.class)
public abstract class PotBlockEntityMixin implements CarrierSetter {

    /** 炒锅的原料槽位（9 槽） */
    @Shadow
    private NonNullList<ItemStack> inputs;

    /** 处理完成的产物 */
    @Shadow
    private ItemStack result;

    /** 炒锅状态：0=放料 1=烹饪中 2=完成 3=烧焦 */
    @Shadow
    private int status;

    /** 当前阶段剩余刻数 */
    @Shadow
    private int currentTick;

    /** 翻炒次数 */
    @Shadow
    private int stirFryCount;

    /** 油脂/奶油原料（Pot 交互时检查 carrier.isEmpty() 确认是否有油） */
    @Shadow
    private Ingredient carrier;

    /**
     * 注入 hasHeatSource 方法：当下方为冷灶时视为存在热源。
     *
     * @param level 世界实例
     * @param cir   回调信息，用于设置返回值
     */
    @Inject(
            method = "hasHeatSource(Lnet/minecraft/world/level/Level;)Z",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void KTC$injectHasHeatSource(Level level, CallbackInfoReturnable<Boolean> cir) {
        if (KTC$hasColdStoveSource(level)) {
            cir.setReturnValue(true);
        }
    }

    /** 手动跟踪的 HAS_OIL 状态 */
    @Unique
    private boolean KTC$manualHasOil = false;

    /** 手动跟踪的 SHOW_OIL 状态 */
    @Unique
    private boolean KTC$manualShowOil = false;

    /**
     * 同步 PotBlock.HAS_OIL 与 SHOW_OIL 的方块状态。
     * <p>
     * 烹饪开始后会清空 carrier（让取成品走 takeOutWithoutCarrier 锅铲路径），
     * 此时用 KTC$manualHasOil / KTC$manualShowOil 手动控制方块状态，
     * 避免 carrier 被清空后油或奶油的视觉效果消失。
     */
    @Unique
    private void KTC$syncHasOilState() {
        Level level = ((PotBlockEntity) (Object) this).getLevel();
        if (level == null) return;
        BlockPos pos = ((PotBlockEntity) (Object) this).getBlockPos();
        BlockState current = level.getBlockState(pos);
        boolean hasOil = this.KTC$manualHasOil
                || (this.carrier != null && !this.carrier.isEmpty());
        boolean showOil = this.KTC$manualShowOil
                || (this.carrier != null && !this.carrier.isEmpty());
        boolean changed = false;
        BlockState newState = current;
        if (current.hasProperty(PotBlock.HAS_OIL)
                && current.getValue(PotBlock.HAS_OIL) != hasOil) {
            newState = newState.setValue(PotBlock.HAS_OIL, hasOil);
            changed = true;
        }
        if (current.hasProperty(PotBlock.SHOW_OIL)
                && current.getValue(PotBlock.SHOW_OIL) != showOil) {
            newState = newState.setValue(PotBlock.SHOW_OIL, showOil);
            changed = true;
        }
        if (changed) {
            level.setBlock(pos, newState, 3);
        }
    }

    /**
     * 烹饪开始时标记油已存在（仅用于交互与视觉）。
     */
    @Unique
    private void KTC$markOilPresent() {
        this.KTC$manualHasOil = true;
        this.KTC$manualShowOil = true;
    }

    /**
     * 烹饪结束或取走成品时清空手动标记。
     */
    @Unique
    private void KTC$clearManualOil() {
        this.KTC$manualHasOil = false;
        this.KTC$manualShowOil = false;
    }

    /**
     * 触发方块实体刷新（同步到客户端）。
     */
    @Unique
    private void KTC$refresh() {
        ((PotBlockEntity) (Object) this).refresh();
    }

    /**
     * CarrierSetter 接口实现：设置炒锅的 carrier。
     *
     * @param ingredient 要设置的油脂原料
     */
    @Override
    @Unique
    public void KTC$setCarrier(Ingredient ingredient) {
        this.carrier = ingredient;
        KTC$syncHasOilState();
        KTC$refresh();
    }

    /** 冷炒配方查询缓存，避免每次 tick 都进行完整配方匹配 */
    @Unique
    private RecipeManager.CachedCheck<SimpleInput, ColdTossRecipe> KTC$coldTossCheck;

    /**
     * 注入炒锅的 tick 方法。
     * <p>
     * 先尝试从下方奶油油壶抽取奶油；若下方不是冷灶则放行原逻辑，
     * 否则执行冷炒逻辑并取消原 tick。
     *
     * @param level 世界实例
     * @param ci    回调信息，用于取消原方法执行
     */
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void KTC$coldTossTick(Level level, CallbackInfo ci) {
        if (this.carrier == null || this.carrier.isEmpty()) {
            KTC$tryExtractCreamFromOilBlock(level);
        }

        if (!KTC$hasColdStoveSource(level)) {
            return;
        }
        ci.cancel();
        KTC$tickColdToss(level);
    }

    /**
     * 尝试从下方奶油油壶抽取奶油到炒锅的 carrier。
     * <p>
     * 奶油油壶方块实体存储奶油数量（0 到 MAX_CREAM_COUNT = 256）。
     * 每次只抽取 1 份奶油并递减计数，奶油油壶像储备罐一样持续供应；
     * 当计数变为 0 时，奶油油壶恢复为原模组的空油壶方块。
     *
     * @param level 世界实例
     */
    @Unique
    private void KTC$tryExtractCreamFromOilBlock(Level level) {
        BlockPos belowPos = ((PotBlockEntity) (Object) this).getBlockPos().below();
        BlockState belowState = level.getBlockState(belowPos);
        if (!belowState.is(KTCBlocks.CREAM_OIL.get())) return;
        if (!(level.getBlockEntity(belowPos) instanceof CreamOilBlockEntity creamOil)) return;
        if (creamOil.getCreamCount() <= 0) return;

        // 抽 1 份奶油到 Pot.carrier
        creamOil.extractCream(1);
        this.carrier = Ingredient.of(KTCItems.CREAM.get());
        KTC$syncHasOilState();
        KTC$refresh();

        // 奶油抽完了 → 变回空 OilPotBlock
        if (creamOil.getCreamCount() <= 0) {
            BlockState restoreState = ModBlocks.OIL_POT.get().defaultBlockState()
                    .setValue(HorizontalDirectionalBlock.FACING,
                            belowState.getValue(HorizontalDirectionalBlock.FACING))
                    .setValue(BlockStateProperties.WATERLOGGED,
                            belowState.getValue(BlockStateProperties.WATERLOGGED));
            level.setBlock(belowPos, restoreState, 3);
        }
    }

    /**
     * 注入 onPlaceOil 方法头部，扩展支持奶油与奶油油壶。
     * <p>
     * 仅在下方为冷灶时生效：手持奶油时直接设置 carrier 并消耗物品；
     * 手持携带奶油数量的奶油油壶时递减其数量并设置 carrier。
     * 其余情况交由原方法处理油脂等。
     *
     * @param level 世界实例
     * @param user  使用物品的实体
     * @param stack 使用的物品堆
     * @param cir   回调信息，用于设置返回值
     */
    @Inject(
            method = "onPlaceOil",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void KTC$onPlaceOilCream(Level level, LivingEntity user, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!KTC$hasColdStoveSource(level)) return;

        // 手持 CREAM 物品
        if (stack.is(KTCItems.CREAM.get())) {
            this.carrier = Ingredient.of(KTCItems.CREAM.get());
            KTC$syncHasOilState();
            KTC$refresh();
            if (!(user instanceof Player player) || !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            cir.setReturnValue(true);
            return;
        }

        // 手持 CREAM_OIL BlockItem（带 CustomData CreamCount）
        if (stack.is(KTCItems.CREAM_OIL.get())) {
            int creamCount = 0;
            if (stack.has(DataComponents.CUSTOM_DATA)) {
                CompoundTag tag = Objects.requireNonNull(stack.get(DataComponents.CUSTOM_DATA)).copyTag();
                creamCount = tag.contains(CreamOilBlock.CREAM_COUNT_NBT)
                        ? tag.getInt(CreamOilBlock.CREAM_COUNT_NBT) : 0;
            }
            if (creamCount > 0) {
                this.carrier = Ingredient.of(KTCItems.CREAM.get());
                KTC$syncHasOilState();
                KTC$refresh();
                creamCount--;
                if (creamCount <= 0) {
                    if (user instanceof Player player) {
                        player.setItemInHand(player.getUsedItemHand(), new ItemStack(ModBlocks.OIL_POT.get()));
                    }
                } else {
                    CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                    tag.putInt(CreamOilBlock.CREAM_COUNT_NBT, creamCount);
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
                cir.setReturnValue(true);
            }
        }
        // 冷灶下也要让原 onPlaceOil 处理油脂等
    }

    // ---- 冷灶检测（与 TeapotBlockEntityMixin 同款逻辑）----

    /** 冷灶方块的缓存引用，避免每次 tick 都查询注册表 */
    @Unique
    private static Block KTC$coldStoveBlock;

    /**
     * 检查炒锅下方是否为 KTC 冷灶。
     *
     * @param level 世界实例
     * @return 下方为已点燃的 KTC 冷灶返回 true
     */
    @Unique
    private boolean KTC$hasColdStoveSource(Level level) {
        BlockPos belowPos = ((PotBlockEntity) (Object) this).getBlockPos().below();
        BlockState belowState = level.getBlockState(belowPos);
        Block coldStove = KTC$getColdStoveBlock();
        if (coldStove != null && belowState.is(coldStove)) {
            // 冷灶需要 LIT=true 才算有效热源
            return !belowState.hasProperty(BlockStateProperties.LIT) || belowState.getValue(BlockStateProperties.LIT);
        }
        return false;
    }

    /**
     * 懒加载冷灶方块实例。
     *
     * @return 冷灶方块实例
     */
    @Unique
    private static Block KTC$getColdStoveBlock() {
        if (KTC$coldStoveBlock == null) {
            KTC$coldStoveBlock = BuiltInRegistries.BLOCK.get(
                    ResourceLocation.fromNamespaceAndPath("kaleidoscope_teachronicles", "cold_stove")
            );
        }
        return KTC$coldStoveBlock;
    }

    /**
     * 冷炒 tick 状态机。
     * <p>
     * 状态流转：0（放料阶段）→ 1（冷炒处理中，按翻炒次数推进）→ 2（完成等待）→ reset。
     * 无热源，不会触发烧焦（status 3）。
     *
     * @param level 世界实例
     */
    @Unique
    private void KTC$tickColdToss(Level level) {
        // status=0: 等待放料 —— inputs 空就等着（油脂可能还在，等玩家放原料）
        if (this.status == 0) {
            if (KTC$isEmpty()) {
                return; // inputs 空，不要调 reset()！reset 会清 carrier！
            }
            // inputs 有东西，立即尝试匹配冷炒配方
            SimpleInput simpleInput = new SimpleInput(this.inputs);
            Optional<RecipeHolder<ColdTossRecipe>> recipeOpt =
                    KTC$getColdTossCheck().getRecipeFor(simpleInput, level);
            if (recipeOpt.isPresent()) {
                ColdTossRecipe recipe = recipeOpt.get().value();
                // 【关键】不要消耗 inputs！原 Pot 也是保留 inputs 供渲染器显示锅内物品。
                // 渲染器读的就是 inputs 里的 ItemStack，清空了就看不见了。
                this.result = recipe.assemble(simpleInput, level.registryAccess());
                this.stirFryCount = recipe.stirFryCount();
                this.currentTick = 0;
                this.status = 1;
                // 烹饪开始后清空 carrier：
                // takeOutProduct 会根据 carrier.isEmpty() 分流——
                // 有 carrier → takeOutWithCarrier（必须手持对应物品！）
                // 无 carrier → takeOutWithoutCarrier（可用锅铲取）
                // 我们用手动标记保持 HAS_OIL/SHOW_OIL 视觉，carrier 只留到取成品前
                KTC$markOilPresent();
                this.carrier = Ingredient.EMPTY;
                KTC$syncHasOilState();
                KTC$refresh();
            }
            return;
        }

        // status=1: 冷炒处理 —— 按翻炒次数推进，归零后进入完成
        if (this.status == 1) {
            if (this.currentTick % 10 == 0) {
                --this.stirFryCount;
                KTC$refresh();
            }
            ++this.currentTick;
            if (this.stirFryCount <= 0) {
                this.status = 2;
                this.currentTick = 800;
                // 与原 Pot.tickCooking 一致：烹饪完成后关闭 SHOW_OIL（视觉效果）
                this.KTC$manualShowOil = false;
                KTC$syncHasOilState();
                KTC$refresh();
            }
            return;
        }

        // status=2: 完成等待 —— 递减 currentTick，但不要 reset！
        // 让玩家用碗/锅铲取走 result 后 Pot 交互逻辑自己处理清理
        if (this.status == 2) {
            if (this.currentTick > 0) {
                --this.currentTick;
                if (this.currentTick % 5 == 0) KTC$refresh();
            }
        }
    }

    /**
     * 判断原料槽位是否全部为空。
     *
     * @return 全部为空返回 true
     */
    @Unique
    private boolean KTC$isEmpty() {
        for (ItemStack stack : inputs) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取冷炒配方查询缓存，首次调用时创建。
     *
     * @return 冷炒配方查询缓存
     */
    @Unique
    private RecipeManager.CachedCheck<SimpleInput, ColdTossRecipe> KTC$getColdTossCheck() {
        if (KTC$coldTossCheck == null) {
            KTC$coldTossCheck = RecipeManager.createCheck(KTCRecipes.COLD_TOSS_RECIPE);
        }
        return KTC$coldTossCheck;
    }
}