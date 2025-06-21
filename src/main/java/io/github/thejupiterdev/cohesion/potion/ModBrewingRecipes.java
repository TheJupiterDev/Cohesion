package io.github.thejupiterdev.cohesion.potion;


import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;

public class ModBrewingRecipes {

    public static void registerBrewingRecipes() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(
                    Potions.AWKWARD,
                    Items.SHULKER_SHELL,
                    ModPotions.LEVITATION_POTION
            );
        });

        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(
                    ModPotions.LEVITATION_POTION,
                    Items.GLOWSTONE_DUST,
                    ModPotions.LONG_LEVITATION_POTION
            );
        });
    }
}