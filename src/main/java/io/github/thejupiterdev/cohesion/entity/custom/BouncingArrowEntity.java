package io.github.thejupiterdev.cohesion.entity.custom;

import io.github.thejupiterdev.cohesion.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BouncingArrowEntity extends ArrowEntity {
    private int bounceCount = 0;
    private static final int MAX_BOUNCES = 6;
    private static final double BOUNCE_DAMPING = 0.8; // Reduces velocity after each bounce

    public BouncingArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
        this.setDamage(2);
    }

    public BouncingArrowEntity(EntityType<? extends ArrowEntity> type, World world, double x, double y, double z) {
        super(type, world);
        this.setPosition(x, y, z);
        this.setDamage(2);
    }

    public BouncingArrowEntity(World world, LivingEntity shooter, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, shooter, stack, shotFrom);
        this.setDamage(2);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (bounceCount < MAX_BOUNCES) {
            bounce(blockHitResult);
            bounceCount++;
        } else {
            super.onBlockHit(blockHitResult);
        }
    }

    private void bounce(BlockHitResult hitResult) {
        Vec3d velocity = this.getVelocity();
        Vec3d normal = Vec3d.of(hitResult.getSide().getVector());

        double dotProduct = velocity.dotProduct(normal);
        Vec3d reflection = velocity.subtract(normal.multiply(2 * dotProduct));

        reflection = reflection.multiply(BOUNCE_DAMPING);

        this.setVelocity(reflection);

        Vec3d pos = this.getPos().add(normal.multiply(0.1));
        this.setPosition(pos.x, pos.y, pos.z);

        this.inGround = false;

        if (!this.getWorld().isClient) {
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                    net.minecraft.sound.SoundEvents.BLOCK_STONE_HIT,
                    this.getSoundCategory(), 0.5F, 1.0F + (this.random.nextFloat() - 0.5F) * 0.2F);
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.BOUNCING_ARROW);
    }

}