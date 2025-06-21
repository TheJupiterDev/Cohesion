package io.github.thejupiterdev.cohesion.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void onUse(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = player.getStackInHand(hand);

        // Only apply to fire charge
        if (!(stack.getItem() instanceof FireChargeItem)) return;

        if (!world.isClient) {
            Vec3d look = player.getRotationVec(1.0F);

            FireballEntity fireball = new FireballEntity(
                    world,
                    player,
                    look,
                    1 // explosion power
            );

            fireball.setVelocity(look.multiply(1.25));

            fireball.setPosition(
                    player.getX() + look.x * 1.5,
                    player.getEyeY() + look.y * 1.5,
                    player.getZ() + look.z * 1.5
            );

            world.spawnEntity(fireball);
        }

        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        cir.setReturnValue(TypedActionResult.success(stack, world.isClient()));
    }
}
