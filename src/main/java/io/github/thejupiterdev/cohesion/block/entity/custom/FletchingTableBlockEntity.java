package io.github.thejupiterdev.cohesion.block.entity.custom;

import io.github.thejupiterdev.cohesion.block.entity.ImplementedInventory;
import io.github.thejupiterdev.cohesion.block.entity.ModBlockEntities;
import io.github.thejupiterdev.cohesion.item.ModItems;
import io.github.thejupiterdev.cohesion.screen.custom.FletchingTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FletchingTableBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    // SLOT ENUM
    private static final int POTION_SLOT_ONE = 0;
    private static final int POTION_SLOT_TWO = 1;
    private static final int ARROW_SLOT = 2;
    private static final int OUTPUT_SLOT = 3;

    private boolean crafted = false;

    // TODO: REMOVE! THIS IS FOR TUTORIAL!
    protected final PropertyDelegate propertyDelegate;
    // private int progress = 0;
    // private int maxProgress = 72;

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



    public void tick(World world, BlockPos pos, BlockState state) {
        if (hasRecipe() && !crafted) {
            craftItem();
            markDirty(world, pos, state);
        }

        if (crafted && outputTaken()) {
            removeStack(POTION_SLOT_ONE, 1);
            removeStack(POTION_SLOT_TWO, 1);
            removeStack(ARROW_SLOT, 1);
            clearOutput();
            crafted = false;
            markDirty(world, pos, state);
        }

        if (!hasRecipe() && !outputTaken()) {
            clearOutput();
        }
    }

    private boolean outputTaken() {
        ItemStack output = new ItemStack(Items.SPECTRAL_ARROW, 3);
        ItemStack currentOutput = this.getStack(OUTPUT_SLOT);

        return crafted && (!ItemStack.areItemsEqual(currentOutput, output) || currentOutput.getCount() < output.getCount());
    }

    private void clearOutput() {
        this.setStack(OUTPUT_SLOT, new ItemStack(Items.AIR));
        crafted = false;
    }


    private void craftItem() {
        this.setStack(OUTPUT_SLOT, new ItemStack(Items.SPECTRAL_ARROW, 3));
        this.crafted = true;
    }

    private boolean hasRecipe() {
        Item input1 = ModItems.ELDER_SHARD;
        Item input2 = ModItems.ELDER_SHARD;
        Item input3 = Items.ARROW;
        ItemStack output = new ItemStack(Items.SPECTRAL_ARROW, 3);

        return this.getStack(POTION_SLOT_ONE).isOf(input1) && this.getStack(POTION_SLOT_TWO).isOf(input2) &&
                this.getStack(ARROW_SLOT).isOf(input3) && canInsertItemIntoOutputSlot(output);
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getItem() == output.getItem();
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
