package io.github.thejupiterdev.cohesion;

import io.github.thejupiterdev.cohesion.entity.ModEntities;
import io.github.thejupiterdev.cohesion.entity.client.DiamondArrowEntityRenderer;
import io.github.thejupiterdev.cohesion.entity.client.GoldArrowEntityRenderer;
import io.github.thejupiterdev.cohesion.entity.client.IronArrowEntityRenderer;
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
    }
}
