package io.github.thejupiterdev.cohesion.item.custom;

import io.github.thejupiterdev.cohesion.entity.custom.DiamondArrowEntity;
import io.github.thejupiterdev.cohesion.entity.custom.RedstoneArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RedstoneArrowItem extends ArrowItem {
    public RedstoneArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new RedstoneArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

}
