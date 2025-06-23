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
        // If we haven't reached max bounces yet, bounce off the surface
        if (bounceCount < MAX_BOUNCES) {
            bounce(blockHitResult);
            bounceCount++;
        } else {
            // After 6 bounces, behave like a normal arrow (stick to surface)
            super.onBlockHit(blockHitResult);
        }
    }

    private void bounce(BlockHitResult hitResult) {
        // Get current velocity
        Vec3d velocity = this.getVelocity();
        Vec3d normal = Vec3d.of(hitResult.getSide().getVector());

        // Calculate reflection: v' = v - 2(v·n)n
        double dotProduct = velocity.dotProduct(normal);
        Vec3d reflection = velocity.subtract(normal.multiply(2 * dotProduct));

        // Apply damping to reduce velocity after bounce
        reflection = reflection.multiply(BOUNCE_DAMPING);

        // Set the new velocity
        this.setVelocity(reflection);

        // Move the arrow slightly away from the surface to prevent getting stuck
        Vec3d pos = this.getPos().add(normal.multiply(0.1));
        this.setPosition(pos.x, pos.y, pos.z);

        // Reset the arrow's ground state so it continues flying
        this.inGround = false;

        // Play bounce sound effect (optional)
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

    // Optional: Add getter for bounce count for debugging or other purposes
    public int getBounceCount() {
        return bounceCount;
    }
}