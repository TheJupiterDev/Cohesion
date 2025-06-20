package io.github.thejupiterdev.cohesion.entity.custom;

import io.github.thejupiterdev.cohesion.block.ModBlocks;
import io.github.thejupiterdev.cohesion.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RedstoneArrowEntity extends ArrowEntity {
    private BlockPos poweredBlockPos = null;
    private BlockState originalBlockState = null;
    private boolean isPowering = false;

    public RedstoneArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
        this.setDamage(2);
    }

    public RedstoneArrowEntity(EntityType<? extends ArrowEntity> type, World world, double x, double y, double z) {
        super(type, world);
        this.setPosition(x, y, z);
        this.setDamage(2);
    }

    public RedstoneArrowEntity(World world, LivingEntity shooter, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, shooter, stack, shotFrom);
        this.setDamage(2);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            BlockPos currentPos = this.getBlockPos();
            boolean shouldPower = this.inGround;

            // Check if our invisible redstone block is still intact
            checkInvisibleRedstoneBlockIntegrity();

            if (shouldPower && !isPowering) {
                // Start powering
                startPoweringRedstone(currentPos);
            } else if (!shouldPower && isPowering) {
                // Stop powering
                stopPoweringRedstone();
            } else if (shouldPower && isPowering && !currentPos.equals(poweredBlockPos)) {
                // Arrow moved while stuck - update position
                stopPoweringRedstone();
                startPoweringRedstone(currentPos);
            }
        }
    }

    private void startPoweringRedstone(BlockPos pos) {
        World world = this.getWorld();

        // Find a suitable position to place the invisible redstone block
        BlockPos targetPos = findSuitableRedstonePosition(pos);
        if (targetPos == null) return;

        // Store the original block state
        originalBlockState = world.getBlockState(targetPos);

        // Only place invisible redstone block if the space is air or replaceable
        if (originalBlockState.isAir() || originalBlockState.isReplaceable()) {
            world.setBlockState(targetPos, ModBlocks.INVISIBLE_REDSTONE_BLOCK.getDefaultState(), Block.NOTIFY_ALL);
            poweredBlockPos = targetPos;
            isPowering = true;
        }
    }

    private void stopPoweringRedstone() {
        if (poweredBlockPos != null && originalBlockState != null) {
            World world = this.getWorld();

            // Restore the original block state
            world.setBlockState(poweredBlockPos, originalBlockState, Block.NOTIFY_ALL);

            poweredBlockPos = null;
            originalBlockState = null;
            isPowering = false;
        }
    }

    private void checkInvisibleRedstoneBlockIntegrity() {
        if (poweredBlockPos != null && isPowering) {
            World world = this.getWorld();
            BlockState currentState = world.getBlockState(poweredBlockPos);

            // If the invisible redstone block is no longer there, clean up
            if (currentState.getBlock() != ModBlocks.INVISIBLE_REDSTONE_BLOCK) {
                // The block was removed somehow - clean up our tracking
                poweredBlockPos = null;
                originalBlockState = null;
                isPowering = false;

                // Also remove the arrow since its power source is gone
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    private BlockPos findSuitableRedstonePosition(BlockPos arrowPos) {
        World world = this.getWorld();

        // Try positions around the arrow to find a suitable spot for the invisible redstone block
        BlockPos[] candidates = {
                arrowPos,                    // Same position as arrow (invisible, so no conflict)
                arrowPos.down(),             // Below arrow
                arrowPos.up(),               // Above arrow
                arrowPos.north(),            // Adjacent positions
                arrowPos.south(),
                arrowPos.east(),
                arrowPos.west()
        };

        for (BlockPos pos : candidates) {
            BlockState state = world.getBlockState(pos);
            // Since the invisible redstone block has no collision, we can place it more liberally
            if (state.isAir() || state.isReplaceable()) {
                return pos;
            }
        }

        return null; // No suitable position found
    }

    @Override
    public void remove(RemovalReason reason) {
        // Stop powering redstone when the arrow is removed
        if (!this.getWorld().isClient) {
            stopPoweringRedstone();
        }
        super.remove(reason);
    }

    @Override
    public boolean isPushable() {
        // Make arrow immune to pistons (though the invisible redstone block is also piston-immune)
        return false;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.REDSTONE_ARROW);
    }
}