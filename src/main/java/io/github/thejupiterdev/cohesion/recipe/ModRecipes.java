package io.github.thejupiterdev.cohesion.recipe;

import io.github.thejupiterdev.cohesion.Cohesion;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<FletchingTableRecipe> FLETCHING_TABLE_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            Identifier.of(Cohesion.MOD_ID, "fletching_table"),
            new FletchingTableRecipe.Serializer()
    );
    public static final RecipeType<FletchingTableRecipe> FLETCHING_TABLE_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(Cohesion.MOD_ID, "fletching_table"),
            new RecipeType<FletchingTableRecipe>() {
                @Override
                public String toString() {
                    return "fletching_table";
                }});

    public static void registerRecipes() {
        Cohesion.LOGGER.info("Registering Custom Recipes for " + Cohesion.MOD_ID);
    }
}
