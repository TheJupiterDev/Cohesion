package io.github.thejupiterdev.cohesion.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
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

import java.util.Optional;

public record FletchingTableRecipe(IngredientWithComponents inputItem1,
                                   IngredientWithComponents inputItem2,
                                   IngredientWithComponents inputItem3,
                                   ItemStack output) implements Recipe<FletchingTableRecipeInput> {

    // Wrapper class to hold both ingredient and required components
    public record IngredientWithComponents(Ingredient ingredient, Optional<ComponentMap> requiredComponents) {
        public static final Codec<IngredientWithComponents> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter(IngredientWithComponents::ingredient),
                ComponentMap.CODEC.optionalFieldOf("components").forGetter(IngredientWithComponents::requiredComponents)
        ).apply(inst, IngredientWithComponents::new));

        // Create a packet codec for optional ComponentMap
        private static final PacketCodec<RegistryByteBuf, Optional<ComponentMap>> OPTIONAL_COMPONENT_MAP_CODEC =
                new PacketCodec<RegistryByteBuf, Optional<ComponentMap>>() {
                    @Override
                    public Optional<ComponentMap> decode(RegistryByteBuf buf) {
                        if (buf.readBoolean()) {
                            // Use NBT for network transmission
                            var nbt = buf.readNbt();
                            if (nbt != null) {
                                try {
                                    var ops = buf.getRegistryManager().getOps(com.mojang.serialization.JavaOps.INSTANCE);
                                    return Optional.of(ComponentMap.CODEC.parse(ops, nbt).getOrThrow());
                                } catch (Exception e) {
                                    return Optional.empty();
                                }
                            }
                        }
                        return Optional.empty();
                    }

                    @Override
                    public void encode(RegistryByteBuf buf, Optional<ComponentMap> value) {
                        buf.writeBoolean(value.isPresent());
                        if (value.isPresent()) {
                            try {
                                var ops = buf.getRegistryManager().getOps(com.mojang.serialization.JavaOps.INSTANCE);
                                var result = ComponentMap.CODEC.encodeStart(ops, value.get()).getOrThrow();
                                if (result instanceof net.minecraft.nbt.NbtElement nbt) {
                                    buf.writeNbt(nbt);
                                } else {
                                    buf.writeNbt(null);
                                }
                            } catch (Exception e) {
                                buf.writeNbt(null);
                            }
                        }
                    }
                };

        public static final PacketCodec<RegistryByteBuf, IngredientWithComponents> PACKET_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, IngredientWithComponents::ingredient,
                        OPTIONAL_COMPONENT_MAP_CODEC, IngredientWithComponents::requiredComponents,
                        IngredientWithComponents::new);

        public boolean test(ItemStack stack) {
            if (!ingredient.test(stack)) return false;

            if (requiredComponents.isEmpty()) return true;

            // Check components
            ComponentMap required = requiredComponents.get();
            ComponentMap stackComponents = stack.getComponents();

            for (ComponentType<?> type : required.getTypes()) {
                Object requiredValue = required.get(type);
                Object stackValue = stackComponents.get(type);

                if (stackValue == null || !requiredValue.equals(stackValue)) {
                    return false;
                }
            }


            return true;
        }


        // Convenience constructor for ingredient-only
        public static IngredientWithComponents of(Ingredient ingredient) {
            return new IngredientWithComponents(ingredient, Optional.empty());
        }

        // Convenience constructor with components
        public static IngredientWithComponents of(Ingredient ingredient, ComponentMap components) {
            return new IngredientWithComponents(ingredient, Optional.of(components));
        }
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem1.ingredient());
        list.add(this.inputItem2.ingredient());
        list.add(this.inputItem3.ingredient());
        return list;
    }

    @Override
    public boolean matches(FletchingTableRecipeInput input, World world) {
        if (world.isClient()) return false;

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
                IngredientWithComponents.CODEC.fieldOf("item_left").forGetter(FletchingTableRecipe::inputItem1),
                IngredientWithComponents.CODEC.fieldOf("item_right").forGetter(FletchingTableRecipe::inputItem2),
                IngredientWithComponents.CODEC.fieldOf("arrow").forGetter(FletchingTableRecipe::inputItem3),
                ItemStack.CODEC.fieldOf("result").forGetter(FletchingTableRecipe::output)
        ).apply(inst, FletchingTableRecipe::new));

        public static final PacketCodec<RegistryByteBuf, FletchingTableRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        IngredientWithComponents.PACKET_CODEC, FletchingTableRecipe::inputItem1,
                        IngredientWithComponents.PACKET_CODEC, FletchingTableRecipe::inputItem2,
                        IngredientWithComponents.PACKET_CODEC, FletchingTableRecipe::inputItem3,
                        ItemStack.PACKET_CODEC, FletchingTableRecipe::output,
                        FletchingTableRecipe::new);

        @Override
        public MapCodec<FletchingTableRecipe> codec() {return CODEC;}

        @Override
        public PacketCodec<RegistryByteBuf, FletchingTableRecipe> packetCodec() {return STREAM_CODEC;}
    }
}