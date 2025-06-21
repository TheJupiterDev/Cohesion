package io.github.thejupiterdev.cohesion.entity.custom;

import io.github.thejupiterdev.cohesion.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PrismaticArrowEntity extends ArrowEntity {
    public PrismaticArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
        this.setDamage(2);
    }

    public PrismaticArrowEntity(EntityType<? extends ArrowEntity> type, World world, double x, double y, double z) {
        super(type, world);
        this.setPosition(x, y, z);
        this.setDamage(2);
    }

    public PrismaticArrowEntity(World world, LivingEntity shooter, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, shooter, stack, shotFrom);
        this.setDamage(2);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isTouchingWater()) {
            // Cancel vanilla water drag by reapplying velocity boost
            Vec3d velocity = this.getVelocity().multiply(1.67); // Adjust multiplier as needed
            this.setVelocity(velocity);

            // Optional: add water particles
            if (this.getWorld().isClient) {
                for (int i = 0; i < 2; i++) {
                    this.getWorld().addParticle(
                            net.minecraft.particle.ParticleTypes.BUBBLE,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            0, 0, 0
                    );
                }
            }
        }
    }



    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.PRISMATIC_ARROW);
    }
}
