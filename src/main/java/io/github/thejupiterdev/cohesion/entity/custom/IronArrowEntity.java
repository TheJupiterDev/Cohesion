package io.github.thejupiterdev.cohesion.entity.custom;

import io.github.thejupiterdev.cohesion.entity.ModEntities;
import io.github.thejupiterdev.cohesion.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class IronArrowEntity extends ArrowEntity {
    public IronArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
        this.setDamage(3);
    }

    public IronArrowEntity(World world, LivingEntity shooter, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, shooter, stack, shotFrom);
        this.setDamage(3);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.IRON_ARROW);
    }
}
