package io.github.thejupiterdev.cohesion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FireballEntity.class)
public class FireballEntityMixin {

    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void onCollisionNoOwnerDamage(HitResult hitResult, CallbackInfo ci) {
        FireballEntity fireball = (FireballEntity) (Object) this;
        World world = fireball.getWorld();
        Entity owner = fireball.getOwner();

        if (!world.isClient) {
            boolean restoreInvulnerable = false;

            if (owner instanceof LivingEntity livingOwner) {
                // Temporarily set invulnerable to avoid damage from explosion
                if (!livingOwner.isInvulnerable()) {
                    livingOwner.setInvulnerable(true);
                    restoreInvulnerable = true;
                }
            }

            // This will break blocks and damage nearby entities
            world.createExplosion(
                    fireball,
                    fireball.getX(),
                    fireball.getY(),
                    fireball.getZ(),
                    2.0F,
                    false,
                    ExplosionSourceType.MOB // MOB explosions break blocks
            );

            // Restore invulnerability state of the owner
            if (owner instanceof LivingEntity livingOwner && restoreInvulnerable) {
                livingOwner.setInvulnerable(false);
            }

            // Manually apply extra knockback/damage to non-owner entities if needed
            double radius = 3.0;
            List<Entity> entities = world.getEntitiesByClass(Entity.class,
                    fireball.getBoundingBox().expand(radius),
                    e -> e.isAlive() && e != owner);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    DamageSource damageSource = new DamageSource(
                            world.getRegistryManager()
                                    .get(RegistryKeys.DAMAGE_TYPE)
                                    .entryOf(DamageTypes.EXPLOSION)
                    );
                    living.damage(damageSource, 1.0f);

                    double dx = entity.getX() - fireball.getX();
                    double dz = entity.getZ() - fireball.getZ();
                    double dist = Math.max(Math.sqrt(dx * dx + dz * dz), 0.1);
                    double strength = 1.5 / dist;

                    entity.addVelocity(dx * strength, 0.5, dz * strength);
                    entity.velocityModified = true;
                }
            }

            fireball.discard();
            ci.cancel();
        }
    }
}
