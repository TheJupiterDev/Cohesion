package io.github.thejupiterdev.cohesion.item;

import io.github.thejupiterdev.cohesion.Cohesion;
import io.github.thejupiterdev.cohesion.block.ModBlocks;
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
                        entries.add(ModBlocks.SUGAR_CUBE);
                        entries.add(ModItems.PRISMATIC_ROD);
                        entries.add(ModItems.GRAPPLE);

                        entries.add(ModItems.IRON_ARROW);
                        entries.add(ModItems.GOLD_ARROW);
                        entries.add(ModItems.DIAMOND_ARROW);
                        entries.add(ModItems.REDSTONE_ARROW);
                        entries.add(ModItems.TORCH_ARROW);
                        entries.add(ModItems.ENDER_ARROW);
                        entries.add(ModItems.PRISMATIC_ARROW);
                        entries.add(ModItems.GRAPPLING_ARROW);
                    }).build());


    public static void registerItemGroups() {
        Cohesion.LOGGER.info("Registering Item Groups for " + Cohesion.MOD_ID);
    }

}
