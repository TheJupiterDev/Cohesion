package io.github.thejupiterdev.cohesion.event;

import io.github.thejupiterdev.cohesion.Cohesion;
import io.github.thejupiterdev.cohesion.screen.custom.FletchingTableScreenHandler;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModCallbacks {
    public static void registerModCallbacks() {
        UseBlockCallback.EVENT.register(ModCallbacks::onUseBlock);
        Cohesion.LOGGER.info("Registering Mod Callbacks for " + Cohesion.MOD_ID);
    }

    private static ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();

        if (world.getBlockState(pos).getBlock() == Blocks.FLETCHING_TABLE) {
            if (!player.isSneaking()) {
                if (!world.isClient()) {
                    player.openHandledScreen(new ExtendedScreenHandlerFactory<BlockPos>() {
                        @Override
                        public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                            return new FletchingTableScreenHandler(syncId, playerInventory, pos);
                        }

                        @Override
                        public Text getDisplayName() {
                            return Text.translatable("block.minecraft.fletching_table");
                        }

                        @Override
                        public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
                            return pos;
                        }
                    });
                }
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }
}