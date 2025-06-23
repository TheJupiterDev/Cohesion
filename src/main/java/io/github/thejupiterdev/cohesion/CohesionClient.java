package io.github.thejupiterdev.cohesion;

import io.github.thejupiterdev.cohesion.entity.ModEntities;
import io.github.thejupiterdev.cohesion.entity.client.*;
import io.github.thejupiterdev.cohesion.screen.ModScreenHandlers;
import io.github.thejupiterdev.cohesion.screen.custom.FletchingTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class CohesionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(){

        HandledScreens.register(ModScreenHandlers.FLETCHING_TABLE_SCREEN_HANDLER, FletchingTableScreen::new);

        EntityRendererRegistry.register(ModEntities.IRON_ARROW, IronArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.GOLD_ARROW, GoldArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.DIAMOND_ARROW, DiamondArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.REDSTONE_ARROW, RedstoneArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TORCH_ARROW, TorchArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.ENDER_ARROW, EnderArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.PRISMATIC_ARROW, PrismaticArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.GRAPPLING_ARROW, GrapplingArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BOUNCING_ARROW, BouncingArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.COBWEB_ARROW, BouncingArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.ECHO_ARROW, BouncingArrowEntityRenderer::new);
    }
}
