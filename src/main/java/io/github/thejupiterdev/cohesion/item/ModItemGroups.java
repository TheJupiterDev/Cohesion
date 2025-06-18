package io.github.thejupiterdev.cohesion.item;

import io.github.thejupiterdev.cohesion.Cohesion;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup COHESION_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Cohesion.MOD_ID, "cohesion_group"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.ELDER_SHARD))
                    .displayName(Text.translatable("itemgroup.cohesion.cohesion_group"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.ELDER_SHARD);
                    }).build());


    public static void registerItemGroups() {
        Cohesion.LOGGER.info("Registering Item Groups for " + Cohesion.MOD_ID);
    }

}
