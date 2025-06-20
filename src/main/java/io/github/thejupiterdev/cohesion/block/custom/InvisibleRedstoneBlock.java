package io.github.thejupiterdev.cohesion.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class InvisibleRedstoneBlock extends Block {

    public InvisibleRedstoneBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // No collision/selection box
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // No collision
        return VoxelShapes.empty();
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        // Fully transparent
        return true;
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        // No ambient occlusion
        return 1.0F;
    }

    @Override
    public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        // No light blocking
        return 0;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        // All sides are invisible
        return true;
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // No camera collision
        return VoxelShapes.empty();
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        // This block provides redstone power
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        // Provide weak redstone power (like redstone dust)
        return 15;
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        // Provide strong redstone power (like redstone block)
        return 15;
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        // Only allow breaking in creative mode, and only if player is sneaking
        if (!player.isCreative() || !player.isSneaking()) {
            return state;
        }
        super.onBreak(world, pos, state, player);
        return state;
    }

    @Override
    public float getHardness() {
        // Make it very hard to break accidentally
        return -1.0F; // Unbreakable like bedrock
    }

    // Custom method to check if block should drop items
    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        // Only allow interaction in creative mode while sneaking
        if (!player.isCreative() || !player.isSneaking()) {
            return;
        }
        super.onBlockBreakStart(state, world, pos, player);
    }


}