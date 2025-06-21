package io.github.thejupiterdev.cohesion.potion;

import io.github.thejupiterdev.cohesion.Cohesion;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModPotions {

    public static final RegistryEntry<Potion> LEVITATION_POTION = register("levitation", new Potion(new StatusEffectInstance(StatusEffects.LEVITATION, 1200, 0)));
    public static final RegistryEntry<Potion> LONG_LEVITATION_POTION = register("long_levitation", new Potion(new StatusEffectInstance(StatusEffects.LEVITATION, 2400, 0)));

    private static RegistryEntry<Potion> register(String name, Potion potion) {
        return Registry.registerReference(Registries.POTION, Identifier.of(Cohesion.MOD_ID, name), potion);
    }

    public static void registerPotions() {
    }
}