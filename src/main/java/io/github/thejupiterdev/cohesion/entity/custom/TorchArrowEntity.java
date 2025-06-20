package io.github.thejupiterdev.cohesion.entity.custom;

import io.github.thejupiterdev.cohesion.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class TorchArrowEntity extends ArrowEntity {
    public TorchArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
        this.setDamage(2);
    }

    public TorchArrowEntity(EntityType<? extends ArrowEntity> type, World world, double x, double y, double z) {
        super(type, world);
        this.setPosition(x, y, z);
        this.setDamage(2);
    }

    public TorchArrowEntity(World world, LivingEntity shooter, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, shooter, stack, shotFrom);
        this.setDamage(2);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.TORCH_ARROW);
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        super.onBlockHit(result);

        if (!this.getWorld().isClient) {
            World world = this.getWorld();
            Direction face = result.getSide();
            BlockPos placePos;
            BlockState torchState = null;

            if (face == Direction.UP) {
                placePos = result.getBlockPos().up();
                if (world.getBlockState(placePos).isAir()) {
                    torchState = Blocks.TORCH.getDefaultState();
                }
            } else if (face.getAxis().isHorizontal()) {
                placePos = result.getBlockPos().offset(face); // in front of the wall
                BlockPos wallBlock = result.getBlockPos();     // the wall to attach to

                if (world.getBlockState(placePos).isAir() &&
                        world.getBlockState(wallBlock).isSolidBlock(world, wallBlock)) {
                    torchState = Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, face);
                }
            } else {
                return; // Ignore DOWN hits
            }

            if (torchState != null && torchState.canPlaceAt(world, placePos)) {
                world.setBlockState(placePos, torchState);
                world.playSound(null, placePos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.BLOCK_PLACE, placePos);
                this.discard();
            }
        }
    }

}
