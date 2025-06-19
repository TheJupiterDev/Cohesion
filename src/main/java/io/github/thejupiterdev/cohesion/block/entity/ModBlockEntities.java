package io.github.thejupiterdev.cohesion.block.entity;

import io.github.thejupiterdev.cohesion.Cohesion;
import io.github.thejupiterdev.cohesion.block.ModBlocks;
import io.github.thejupiterdev.cohesion.block.entity.custom.FletchingTableBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static final BlockEntityType<FletchingTableBlockEntity> FLETCHING_TABLE_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Cohesion.MOD_ID, "fletching_table_be"),
                    BlockEntityType.Builder.create(FletchingTableBlockEntity::new,
                            ModBlocks.FLETCHING_TABLE).build(null));

    public static void registerBlockEntities() {
        Cohesion.LOGGER.info("Registering Block Entities for " + Cohesion.MOD_ID);
    }
}
