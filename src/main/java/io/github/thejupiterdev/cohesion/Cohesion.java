package io.github.thejupiterdev.cohesion;

import io.github.thejupiterdev.cohesion.block.ModBlocks;
import io.github.thejupiterdev.cohesion.block.entity.ModBlockEntities;
import io.github.thejupiterdev.cohesion.entity.ModEntities;
import io.github.thejupiterdev.cohesion.event.ModCallbacks;
import io.github.thejupiterdev.cohesion.item.ModItemGroups;
import io.github.thejupiterdev.cohesion.item.ModItems;
import io.github.thejupiterdev.cohesion.recipe.ModRecipes;
import io.github.thejupiterdev.cohesion.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cohesion implements ModInitializer {
	public static final String MOD_ID = "cohesion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
		ModCallbacks.registerModCallbacks();
		ModRecipes.registerRecipes();
		ModEntities.registerModEntities();
	}

}