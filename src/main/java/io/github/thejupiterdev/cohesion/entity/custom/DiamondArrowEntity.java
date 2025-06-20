package io.github.thejupiterdev.cohesion.entity.custom;

import io.github.thejupiterdev.cohesion.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DiamondArrowEntity extends ArrowEntity {
    public DiamondArrowEntity(EntityType<? extends ArrowEntity> type, World world) {
        super(type, world);
        this.setDamage(4);
    }

    public DiamondArrowEntity(EntityType<? extends ArrowEntity> type, World world, double x, double y, double z) {
        super(type, world);
        this.setPosition(x, y, z);
        this.setDamage(4);
    }

    public DiamondArrowEntity(World world, LivingEntity shooter, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(world, shooter, stack, shotFrom);
        this.setDamage(4);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.DIAMOND_ARROW);
    }
}
