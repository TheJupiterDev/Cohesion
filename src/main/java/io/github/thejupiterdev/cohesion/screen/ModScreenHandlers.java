package io.github.thejupiterdev.cohesion.screen;

import io.github.thejupiterdev.cohesion.Cohesion;
import io.github.thejupiterdev.cohesion.screen.custom.FletchingTableScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<FletchingTableScreenHandler> FLETCHING_TABLE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(Cohesion.MOD_ID, "fletching_table_screen_handler"),
                    new ScreenHandlerType<>(FletchingTableScreenHandler::new, null));

    public static void registerScreenHandlers() {
        Cohesion.LOGGER.info("Registering Screen Handlers for " + Cohesion.MOD_ID);
    }
}