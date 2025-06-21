package io.github.thejupiterdev.cohesion.entity.custom;

import io.github.thejupiterdev.cohesion.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GrapplingArrowEntity extends ArrowEntity {
    private boolean pulling = false;
    private int pullTicks = 0;
    private final int maxPullTicks = 10;
    private LivingEntity ownerRef = null;

    public GrapplingArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
        this.setDamage(0);
    }

    public GrapplingArrowEntity(EntityType<? extends ArrowEntity> type, World world, double x, double y, double z) {
        super(type, world);
        this.setPosition(x, y, z);
        this.setDamage(0);
    }

    public GrapplingArrowEntity(World world, LivingEntity shooter, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, shooter, stack, shotFrom);
        this.setDamage(0);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.IRON_ARROW);
    }

    @Override
    public void tick() {
        // First, let vanilla arrow logic run
        super.tick();

        // Then check if it's embedded and start pulling
        if (!this.getWorld().isClient && this.inGround && !pulling) {
            if (this.getOwner() instanceof LivingEntity owner) {
                pulling = true;
                pullTicks = 0;
                ownerRef = owner;
            }
        }

        // If we're pulling, apply velocity each tick
        if (pulling && ownerRef != null) {
            double distance = this.getPos().distanceTo(ownerRef.getEyePos());

            if (pullTicks < maxPullTicks && distance > 1.5) {
                Vec3d direction = this.getPos().subtract(ownerRef.getEyePos()).normalize();
                Vec3d pull = direction.multiply(0.5);

                ownerRef.addVelocity(pull.x, pull.y + 0.1, pull.z);
                ownerRef.velocityModified = true;

                pullTicks++;
            } else {
                this.discard(); // Stop pulling and remove the arrow
            }
        }

        if (pulling && !this.getWorld().isClient) {
            Vec3d start = ownerRef.getPos().add(0, ownerRef.getStandingEyeHeight() - 0.35, 0);
            Vec3d end = this.getPos();
            Vec3d diff = end.subtract(start);
            int segments = 10;

            for (int i = 0; i < segments; i++) {
                double t = i / (double) segments;
                Vec3d point = start.add(diff.multiply(t));
                ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.CRIT, point.x, point.y, point.z, 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!this.getWorld().isClient) {
            this.setDamage(0); // prevent damage if you want a pure grapple
        }
        super.onEntityHit(entityHitResult);
    }
}
