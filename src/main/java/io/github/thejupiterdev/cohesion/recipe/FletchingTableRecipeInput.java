package io.github.thejupiterdev.cohesion.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record FletchingTableRecipeInput(ItemStack leftItem, ItemStack rightItem, ItemStack arrow) implements RecipeInput {

    @Override
    public ItemStack getStackInSlot(int slot) {
        return switch (slot) {
            case 0 -> leftItem;
            case 1 -> rightItem;
            case 2 -> arrow;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int getSize() {
        return 3;
    }

    public boolean isEmpty() {
        return leftItem.isEmpty() && rightItem.isEmpty() && arrow.isEmpty();
    }
}