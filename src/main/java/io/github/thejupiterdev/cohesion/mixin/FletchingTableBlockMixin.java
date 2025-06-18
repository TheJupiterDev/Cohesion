package io.github.thejupiterdev.cohesion.mixin;

import io.github.thejupiterdev.cohesion.screen.custom.FletchingTableScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FletchingTableBlock.class)
public class FletchingTableBlockMixin {

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    protected void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!player.isSneaking() && !world.isClient()) {
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inventory, playerEntity) -> new FletchingTableScreenHandler(syncId, inventory),
                    Text.translatable("block.minecraft.fletching_table") // Use vanilla translation or your own
            ));
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}