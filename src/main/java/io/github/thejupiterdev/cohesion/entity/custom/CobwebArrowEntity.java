package io.github.thejupiterdev.cohesion.entity.custom;

import io.github.thejupiterdev.cohesion.item.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CobwebArrowEntity extends ArrowEntity {
    public CobwebArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
        this.setDamage(2);
    }

    public CobwebArrowEntity(EntityType<? extends ArrowEntity> type, World world, double x, double y, double z) {
        super(type, world);
        this.setPosition(x, y, z);
        this.setDamage(2);
    }

    public CobwebArrowEntity(World world, LivingEntity shooter, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, shooter, stack, shotFrom);
        this.setDamage(2);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockPos hitBlockPos = blockHitResult.getBlockPos();
        BlockPos cobwebPos = hitBlockPos.offset(blockHitResult.getSide());
        placeCobwebAndRemove(cobwebPos);
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        BlockPos pos = entityHitResult.getEntity().getBlockPos();
        placeCobwebAndRemove(pos);
        super.onEntityHit(entityHitResult);
    }

    private void placeCobwebAndRemove(BlockPos hitPos) {
        if (!this.getWorld().isClient) {
            World world = this.getWorld();

            if (canPlaceCobweb(world, hitPos)) {
                world.setBlockState(hitPos, Blocks.COBWEB.getDefaultState());
            } else {
                BlockPos[] adjacentPositions = {
                        hitPos.up(),
                        hitPos.down(),
                        hitPos.north(),
                        hitPos.south(),
                        hitPos.east(),
                        hitPos.west()
                };

                for (BlockPos pos : adjacentPositions) {
                    if (canPlaceCobweb(world, pos)) {
                        world.setBlockState(pos, Blocks.COBWEB.getDefaultState());
                        break;
                    }
                }
            }

            // Play cobweb placement sound
            world.playSound(null, this.getX(), this.getY(), this.getZ(),
                    net.minecraft.sound.SoundEvents.BLOCK_WOOL_PLACE,
                    this.getSoundCategory(), 1.0F, 0.8F);

            // Remove the arrow entity
            this.discard();
        }
    }

    private boolean canPlaceCobweb(World world, BlockPos pos) {
        return world.isInBuildLimit(pos) &&
                (world.getBlockState(pos).isAir() ||
                        world.getBlockState(pos).isReplaceable());
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.COBWEB_ARROW);
    }
}