package io.github.thejupiterdev.cohesion.item;

import io.github.thejupiterdev.cohesion.Cohesion;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;



public class ModItems {
    public static final Item ELDER_SHARD = registerItem("elder_shard", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Cohesion.MOD_ID, name), item);
    }

    public static void registerModItems(){
        Cohesion.LOGGER.info("Registering Mod Items for " + Cohesion.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(ELDER_SHARD);
        });
    }

}
