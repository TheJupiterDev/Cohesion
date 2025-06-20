package io.github.thejupiterdev.cohesion.item.custom;

import io.github.thejupiterdev.cohesion.entity.ModEntities;
import io.github.thejupiterdev.cohesion.entity.custom.IronArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class IronArrowItem extends ArrowItem {
    public IronArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new IronArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

}
