package io.github.thejupiterdev.cohesion.mixin;

import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Items.class)
public abstract class StackSizeMixin {

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 0),
            slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;CAKE:Lnet/minecraft/block/Block;")))
    private static int changeCakeMaxCount(int original) {
        return 64;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=snowball")))
    private static int changeSnowballMaxCount(int original) {
        return 64;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item$Settings;maxCount(I)Lnet/minecraft/item/Item$Settings;", ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=saddle")))
    private static int changeSaddleMaxCount(int original) {
        return 16;
    }
}