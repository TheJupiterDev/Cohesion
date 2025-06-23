package io.github.thejupiterdev.cohesion.entity.custom;

import io.github.thejupiterdev.cohesion.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EchoArrowEntity extends ArrowEntity {
    private static final double GLOW_RADIUS = 8.0;
    private static final int GLOW_DURATION = 200; // 10 seconds

    public EchoArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
        this.setDamage(2);
    }

    public EchoArrowEntity(EntityType<? extends ArrowEntity> type, World world, double x, double y, double z) {
        super(type, world);
        this.setPosition(x, y, z);
        this.setDamage(2);
    }

    public EchoArrowEntity(World world, LivingEntity shooter, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, shooter, stack, shotFrom);
        this.setDamage(2);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        applyGlowEffect();
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        applyGlowEffect();
        super.onEntityHit(entityHitResult);
    }

    private void applyGlowEffect() {
        if (!this.getWorld().isClient) {
            World world = this.getWorld();

            // Create a bounding box around the arrow's position
            Box effectArea = new Box(
                    this.getX() - GLOW_RADIUS, this.getY() - GLOW_RADIUS, this.getZ() - GLOW_RADIUS,
                    this.getX() + GLOW_RADIUS, this.getY() + GLOW_RADIUS, this.getZ() + GLOW_RADIUS
            );

            // Find all living entities within the radius
            List<LivingEntity> nearbyEntities = world.getEntitiesByClass(
                    LivingEntity.class, effectArea, entity -> entity != this.getOwner()
            );

            // Apply glowing effect to all found entities
            for (LivingEntity entity : nearbyEntities) {
                entity.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.GLOWING,
                        GLOW_DURATION,
                        0, // Amplifier (0 = level 1)
                        false, // Ambient
                        true,  // Show particles
                        true   // Show icon
                ));
            }

            createEchoEffects(world, nearbyEntities.size());

            this.discard();
        }
    }

    private void createEchoEffects(World world, int entitiesAffected) {
        if (world instanceof ServerWorld serverWorld) {
            // Create a pulse of particles around the impact point
            for (int i = 0; i < 50; i++) {
                double offsetX = (world.random.nextDouble() - 0.5) * GLOW_RADIUS * 2;
                double offsetY = (world.random.nextDouble() - 0.5) * GLOW_RADIUS * 2;
                double offsetZ = (world.random.nextDouble() - 0.5) * GLOW_RADIUS * 2;

                serverWorld.spawnParticles(
                        ParticleTypes.SONIC_BOOM,
                        this.getX() + offsetX,
                        this.getY() + offsetY,
                        this.getZ() + offsetZ,
                        1, 0, 0, 0, 0
                );
            }

            for (int i = 0; i < 30; i++) {
                double offsetX = (world.random.nextDouble() - 0.5) * GLOW_RADIUS;
                double offsetY = (world.random.nextDouble() - 0.5) * GLOW_RADIUS;
                double offsetZ = (world.random.nextDouble() - 0.5) * GLOW_RADIUS;

                serverWorld.spawnParticles(
                        ParticleTypes.END_ROD,
                        this.getX() + offsetX,
                        this.getY() + offsetY,
                        this.getZ() + offsetZ,
                        1, 0, 0, 0, 0.1
                );
            }
        }

        // Play echo sound effect - louder if more entities were affected
        float volume = Math.min(1.0f, 0.5f + (entitiesAffected * 0.1f));
        world.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK,
                this.getSoundCategory(), volume, 1.2f);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.IRON_ARROW);
    }
}