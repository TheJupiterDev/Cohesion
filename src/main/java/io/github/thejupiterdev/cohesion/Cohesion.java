package io.github.thejupiterdev.cohesion;

import io.github.thejupiterdev.cohesion.item.ModItemGroups;
import io.github.thejupiterdev.cohesion.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cohesion implements ModInitializer {
	public static final String MOD_ID = "cohesion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
	}
}