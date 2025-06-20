package io.github.thejupiterdev.cohesion.item;

import io.github.thejupiterdev.cohesion.Cohesion;
import io.github.thejupiterdev.cohesion.item.custom.DiamondArrowItem;
import io.github.thejupiterdev.cohesion.item.custom.GoldArrowItem;
import io.github.thejupiterdev.cohesion.item.custom.IronArrowItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;



public class ModItems {
    public static final Item ELDER_SHARD = registerItem("elder_shard", new Item(new Item.Settings()));

    public static final Item IRON_ARROW = registerItem("iron_arrow", new IronArrowItem(new Item.Settings()));
    public static final Item GOLD_ARROW = registerItem("gold_arrow", new GoldArrowItem(new Item.Settings()));
    public static final Item DIAMOND_ARROW = registerItem("diamond_arrow", new DiamondArrowItem(new Item.Settings()));

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
