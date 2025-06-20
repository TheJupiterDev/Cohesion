package io.github.thejupiterdev.cohesion.entity;

import io.github.thejupiterdev.cohesion.Cohesion;
import io.github.thejupiterdev.cohesion.entity.custom.GoldArrowEntity;
import io.github.thejupiterdev.cohesion.entity.custom.IronArrowEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<IronArrowEntity> IRON_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Cohesion.MOD_ID, "iron_arrow"),
            EntityType.Builder.<IronArrowEntity>create(IronArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .build()
    );

    public static final EntityType<GoldArrowEntity> GOLD_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Cohesion.MOD_ID, "gold_arrow"),
            EntityType.Builder.<GoldArrowEntity>create(GoldArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .build()
    );

    public static void registerModEntities() {
        Cohesion.LOGGER.info("Registering Mod Entities for " + Cohesion.MOD_ID);
    }

}
