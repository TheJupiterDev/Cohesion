package io.github.thejupiterdev.cohesion;

import io.github.thejupiterdev.cohesion.screen.ModScreenHandlers;
import io.github.thejupiterdev.cohesion.screen.custom.FletchingTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class CohesionClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(){

        HandledScreens.register(ModScreenHandlers.FLETCHING_TABLE_SCREEN_HANDLER, FletchingTableScreen::new);
    }
}
