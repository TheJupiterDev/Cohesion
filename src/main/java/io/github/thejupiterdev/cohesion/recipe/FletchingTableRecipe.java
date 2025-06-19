package io.github.thejupiterdev.cohesion.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public record FletchingTableRecipe(Ingredient inputItem1,
                                   Ingredient inputItem2,
                                   Ingredient inputItem3,
                                   ItemStack output) implements Recipe<FletchingTableRecipeInput> {
    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem1);
        list.add(this.inputItem2);
        list.add(this.inputItem3);
        return list;
    }

    @Override
    public boolean matches(FletchingTableRecipeInput input, World world) {
        if(world.isClient()) {
            return false;
        }

        return inputItem1.test(input.getStackInSlot(0)) &&
                inputItem2.test(input.getStackInSlot(1)) &&
                inputItem3.test(input.getStackInSlot(2));
    }

    @Override
    public ItemStack craft(FletchingTableRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FLETCHING_TABLE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FLETCHING_TABLE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<FletchingTableRecipe> {
        public static final MapCodec<FletchingTableRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("item_left").forGetter(FletchingTableRecipe::inputItem1),
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("item_right").forGetter(FletchingTableRecipe::inputItem2),
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("arrow").forGetter(FletchingTableRecipe::inputItem3),
                ItemStack.CODEC.fieldOf("result").forGetter(FletchingTableRecipe::output)
        ).apply(inst, FletchingTableRecipe::new));

        public static final PacketCodec<RegistryByteBuf, FletchingTableRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, FletchingTableRecipe::inputItem1,
                        Ingredient.PACKET_CODEC, FletchingTableRecipe::inputItem2,
                        Ingredient.PACKET_CODEC, FletchingTableRecipe::inputItem3,
                        ItemStack.PACKET_CODEC, FletchingTableRecipe::output,
                        FletchingTableRecipe::new);

        @Override
        public MapCodec<FletchingTableRecipe> codec() {return CODEC;}

        @Override
        public PacketCodec<RegistryByteBuf, FletchingTableRecipe> packetCodec() {return STREAM_CODEC;}
    }
}
