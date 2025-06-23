package io.github.thejupiterdev.cohesion.entity;

import io.github.thejupiterdev.cohesion.Cohesion;
import io.github.thejupiterdev.cohesion.entity.custom.*;
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

    public static final EntityType<DiamondArrowEntity> DIAMOND_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Cohesion.MOD_ID, "gold_arrow"),
            EntityType.Builder.<DiamondArrowEntity>create(DiamondArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .build()
    );

    public static final EntityType<RedstoneArrowEntity> REDSTONE_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Cohesion.MOD_ID, "redstone_arrow"),
            EntityType.Builder.<RedstoneArrowEntity>create(RedstoneArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .build()
    );

    public static final EntityType<TorchArrowEntity> TORCH_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Cohesion.MOD_ID, "torch_arrow"),
            EntityType.Builder.<TorchArrowEntity>create(TorchArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .build()
    );

    public static final EntityType<EnderArrowEntity> ENDER_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Cohesion.MOD_ID, "ender_arrow"),
            EntityType.Builder.<EnderArrowEntity>create(EnderArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .build()
    );

    public static final EntityType<PrismaticArrowEntity> PRISMATIC_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Cohesion.MOD_ID, "prismatic_arrow"),
            EntityType.Builder.<PrismaticArrowEntity>create(PrismaticArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .build()
    );

    public static final EntityType<GrapplingArrowEntity> GRAPPLING_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Cohesion.MOD_ID, "grappling_arrow"),
            EntityType.Builder.<GrapplingArrowEntity>create(GrapplingArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .build()
    );

    public static final EntityType<BouncingArrowEntity> BOUNCING_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Cohesion.MOD_ID, "bouncing_arrow"),
            EntityType.Builder.<BouncingArrowEntity>create(BouncingArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .build()
    );

    public static final EntityType<CobwebArrowEntity> COBWEB_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Cohesion.MOD_ID, "cobweb_arrow"),
            EntityType.Builder.<CobwebArrowEntity>create(CobwebArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .build()
    );

    public static final EntityType<EchoArrowEntity> ECHO_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Cohesion.MOD_ID, "echo_arrow"),
            EntityType.Builder.<EchoArrowEntity>create(EchoArrowEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .build()
    );

    public static void registerModEntities() {
        Cohesion.LOGGER.info("Registering Mod Entities for " + Cohesion.MOD_ID);
    }

}
