package io.github.thejupiterdev.cohesion.block.entity.custom;

import com.mojang.brigadier.Command;
import io.github.thejupiterdev.cohesion.block.entity.ImplementedInventory;
import io.github.thejupiterdev.cohesion.block.entity.ModBlockEntities;
import io.github.thejupiterdev.cohesion.recipe.FletchingTableRecipe;
import io.github.thejupiterdev.cohesion.recipe.FletchingTableRecipeInput;
import io.github.thejupiterdev.cohesion.recipe.ModRecipes;
import io.github.thejupiterdev.cohesion.screen.custom.FletchingTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FletchingTableBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    // SLOT ENUM
    private static final int POTION_SLOT_ONE = 0;
    private static final int POTION_SLOT_TWO = 1;
    private static final int ARROW_SLOT = 2;
    private static final int OUTPUT_SLOT = 3;

    private boolean crafted = false;

    private ItemStack lastCraftedOutput = ItemStack.EMPTY;

    // TODO: REMOVE?
    protected final PropertyDelegate propertyDelegate;

    public FletchingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLETCHING_TABLE_BE, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return 0;
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.cohesion.fletching_table");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new FletchingTableScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    // Logic

    public void tick(World world, BlockPos pos, BlockState state) {
        if (hasRecipe() && !crafted) {
            craftItem();
            markDirty(world, pos, state);
        }
        if (crafted && outputTaken()) {
            crafted = false;
            removeStack(POTION_SLOT_ONE, 1);
            removeStack(POTION_SLOT_TWO, 1);
            removeStack(ARROW_SLOT, 1);
            clearOutput();
            markDirty(world, pos, state);
        }
        if (!hasRecipe() && !outputTaken()) {
            clearOutput();
        }
    }

    private boolean outputTaken() {
        ItemStack currentOutput = this.getStack(OUTPUT_SLOT);
        return crafted && (currentOutput.isEmpty() || !ItemStack.areItemsEqual(currentOutput, lastCraftedOutput));
    }


    private void clearOutput() {
        this.setStack(OUTPUT_SLOT, new ItemStack(Items.AIR));
        crafted = false;
    }

    private void craftItem() {
        Optional<RecipeEntry<FletchingTableRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return;

        ItemStack output = recipe.get().value().output().copy();
        this.setStack(OUTPUT_SLOT, output);
        this.lastCraftedOutput = output.copy();
        this.crafted = true;
    }

    private boolean hasRecipe() {
        Optional<RecipeEntry<FletchingTableRecipe>> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return false;

        ItemStack output = recipe.get().value().output();
        return canInsertItemIntoOutputSlot(output);
    }


    private Optional<RecipeEntry<FletchingTableRecipe>> getCurrentRecipe() {
        return this.getWorld().getRecipeManager()
                .getFirstMatch(ModRecipes.FLETCHING_TABLE_TYPE, new FletchingTableRecipeInput(
                        inventory.get(POTION_SLOT_ONE), inventory.get(POTION_SLOT_TWO), inventory.get(ARROW_SLOT)),
                        this.getWorld());
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        ItemStack current = getStack(OUTPUT_SLOT);
        return current.isEmpty() ||
                (ItemStack.areItemsEqual(current, output) &&
                        ItemStack.areItemsAndComponentsEqual(current, output) &&
                        current.getCount() + output.getCount() <= current.getMaxCount());
    }

    // Reading & Writing NBT of block
    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
        nbt.putBoolean("crafted", this.crafted);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        Inventories.readNbt(nbt, inventory, registryLookup);
        this.crafted = nbt.getBoolean("crafted");
        super.readNbt(nbt, registryLookup);
    }


    // SYNC Client & Server stuff
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

}
