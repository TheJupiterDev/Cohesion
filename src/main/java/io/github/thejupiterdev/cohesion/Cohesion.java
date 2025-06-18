package io.github.thejupiterdev.cohesion;

import io.github.thejupiterdev.cohesion.block.ModBlocks;
import io.github.thejupiterdev.cohesion.item.ModItemGroups;
import io.github.thejupiterdev.cohesion.item.ModItems;
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
		//ModBlocks.registerModBlocks();
		ModScreenHandlers.registerScreenHandlers();
	}
}