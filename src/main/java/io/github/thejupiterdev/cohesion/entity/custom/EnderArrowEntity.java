package io.github.thejupiterdev.cohesion.entity.custom;

import io.github.thejupiterdev.cohesion.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EnderArrowEntity extends ArrowEntity {
    public EnderArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
        this.setDamage(2);
    }

    public EnderArrowEntity(EntityType<? extends ArrowEntity> type, World world, double x, double y, double z) {
        super(type, world);
        this.setPosition(x, y, z);
        this.setDamage(2);
    }

    public EnderArrowEntity(World world, LivingEntity shooter, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, shooter, stack, shotFrom);
        this.setDamage(2);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.ENDER_ARROW);
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        super.onBlockHit(result);
        teleportOwner();
    }

    @Override
    protected void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        teleportOwner();
    }

    private void teleportOwner() {
        if (!this.getWorld().isClient) {
            LivingEntity owner = (LivingEntity) this.getOwner();
            if (owner != null && owner.isAlive()) {
                Vec3d pos = this.getPos();

                // Particle & sound
                for (int i = 0; i < 32; i++) {
                    this.getWorld().addParticle(ParticleTypes.PORTAL,
                            pos.x + (this.random.nextDouble() - 0.5) * 2.0,
                            pos.y + this.random.nextDouble() * 2.0,
                            pos.z + (this.random.nextDouble() - 0.5) * 2.0,
                            0, 0, 0);
                }

                this.getWorld().playSound(null, pos.x, pos.y, pos.z,
                        SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS,
                        1.0F, 1.0F);

                // Teleport owner
                owner.requestTeleport(pos.x, pos.y, pos.z);
                owner.onLanding();

                // Fall damage
                RegistryEntry<DamageType> fallDamageType = this.getWorld().getRegistryManager()
                        .get(RegistryKeys.DAMAGE_TYPE)
                        .getEntry(DamageTypes.FALL)
                        .orElseThrow();
                DamageSource fallDamage = new DamageSource(fallDamageType);
                owner.damage(fallDamage, 5.0F);
            }

            this.discard();
        }
    }

}
