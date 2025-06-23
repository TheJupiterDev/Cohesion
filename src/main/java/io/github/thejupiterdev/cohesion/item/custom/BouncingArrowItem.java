package io.github.thejupiterdev.cohesion.item.custom;

import io.github.thejupiterdev.cohesion.entity.custom.BouncingArrowEntity;
import io.github.thejupiterdev.cohesion.entity.custom.IronArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BouncingArrowItem extends ArrowItem {
    public BouncingArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new BouncingArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

}
