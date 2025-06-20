package io.github.thejupiterdev.cohesion.block;

import io.github.thejupiterdev.cohesion.Cohesion;
import io.github.thejupiterdev.cohesion.block.custom.InvisibleRedstoneBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block SUGAR_CUBE = registerBlock("sugar_cube",
            new Block(AbstractBlock.Settings.create().hardness(0.6f)
                    .sounds(BlockSoundGroup.GRAVEL)));

    public static final Block INVISIBLE_REDSTONE_BLOCK = Registry.register(
            Registries.BLOCK,
            Identifier.of("cohesion", "invisible_redstone_block"),
            new InvisibleRedstoneBlock(AbstractBlock.Settings.create()
                    .strength(-1.0F, 3600000.0F) // Unbreakable like bedrock
                    .dropsNothing()
                    .noCollision()
                    .nonOpaque()
                    .allowsSpawning((state, world, pos, type) -> false)
                    .solidBlock((state, world, pos) -> false)
                    .suffocates((state, world, pos) -> false)
                    .blockVision((state, world, pos) -> false)
            )
    );

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Cohesion.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Cohesion.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
        Cohesion.LOGGER.info("Registering Mod Blocks for " + Cohesion.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(ModBlocks.SUGAR_CUBE);
        });
    }
}
