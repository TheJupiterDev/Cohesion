package io.github.thejupiterdev.cohesion.screen.custom;

import io.github.thejupiterdev.cohesion.recipe.FletchingTableRecipe;
import io.github.thejupiterdev.cohesion.recipe.FletchingTableRecipeInput;
import io.github.thejupiterdev.cohesion.recipe.ModRecipes;
import io.github.thejupiterdev.cohesion.screen.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class FletchingTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final ScreenHandlerContext context;
    private final World world;

    // Constructor for server-side (with BlockPos)
    public FletchingTableScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, new SimpleInventory(4), new ArrayPropertyDelegate(4),
                ScreenHandlerContext.create(playerInventory.player.getWorld(), pos));
    }

    // Constructor for client-side (without BlockPos)
    public FletchingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(4), new ArrayPropertyDelegate(4),
                ScreenHandlerContext.EMPTY);
    }

    // Main constructor
    public FletchingTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory,
                                       PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
        super(ModScreenHandlers.FLETCHING_TABLE_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.context = context;
        this.world = playerInventory.player.getWorld();

        // Make sure inventory is accessible to the player
        inventory.onOpen(playerInventory.player);

        // Add fletching table input slots with change detection
        this.addSlot(new Slot(this.inventory, 0, 13, 26) {
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                updateOutput();
            }

            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                updateOutput();
            }
        });

        this.addSlot(new Slot(this.inventory, 1, 33, 26) {
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                updateOutput();
            }

            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                updateOutput();
            }
        });

        this.addSlot(new Slot(this.inventory, 2, 23, 45) {
            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                super.onTakeItem(player, stack);
                updateOutput();
            }

            @Override
            public void setStack(ItemStack stack) {
                super.setStack(stack);
                updateOutput();
            }
        });

        // Output slot (can't insert items, handles recipe result)
        this.addSlot(new Slot(this.inventory, 3, 134, 32) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                // Consume input items when output is taken
                inventory.getStack(0).decrement(1);
                inventory.getStack(1).decrement(1);
                inventory.getStack(2).decrement(1);
                updateOutput();
                super.onTakeItem(player, stack);
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(propertyDelegate);

        // Initial output update
        updateOutput();
    }

    private void updateOutput() {
        if (world.isClient()) return;

        FletchingTableRecipeInput recipeInput = new FletchingTableRecipeInput(
                inventory.getStack(0),
                inventory.getStack(1),
                inventory.getStack(2)
        );

        Optional<RecipeEntry<FletchingTableRecipe>> recipe = world.getRecipeManager()
                .getFirstMatch(ModRecipes.FLETCHING_TABLE_TYPE, recipeInput, world);

        if (recipe.isPresent()) {
            ItemStack result = recipe.get().value().craft(recipeInput, world.getRegistryManager());
            inventory.setStack(3, result);
            inventory.markDirty();
        } else {
            inventory.setStack(3, ItemStack.EMPTY);
            inventory.markDirty();
        }
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if(!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, net.minecraft.block.Blocks.FLETCHING_TABLE);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> {
            this.dropInventory(player, this.inventory);
        });
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}