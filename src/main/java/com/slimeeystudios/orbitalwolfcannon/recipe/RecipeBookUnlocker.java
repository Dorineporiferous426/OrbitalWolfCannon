package com.slimeeystudios.orbitalwolfcannon.recipe;

import com.slimeeystudios.orbitalwolfcannon.OrbitalWolfCannonMod;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.registry.RegistryKey;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public final class RecipeBookUnlocker {
    private RecipeBookUnlocker() {
    }

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> unlockOrbitalRecipe(handler.player));
    }

    private static void unlockOrbitalRecipe(ServerPlayerEntity player) {
        ServerWorld world = (ServerWorld) player.getEntityWorld();
        Identifier recipeId = Identifier.of(OrbitalWolfCannonMod.MOD_ID, "orbital_wolf_rod");
        RegistryKey<Recipe<?>> recipeKey = RegistryKey.of(net.minecraft.registry.RegistryKeys.RECIPE, recipeId);
        List<RecipeEntry<?>> recipesToUnlock = new ArrayList<>();

        world.getRecipeManager().get(recipeKey).ifPresent(recipesToUnlock::add);

        if (recipesToUnlock.isEmpty()) {
            for (RecipeEntry<?> entry : world.getRecipeManager().values()) {
                if (entry.id().getValue().getNamespace().equals(OrbitalWolfCannonMod.MOD_ID)) {
                    recipesToUnlock.add(entry);
                }
            }
        }

        if (!recipesToUnlock.isEmpty()) {
            player.unlockRecipes(recipesToUnlock);
        } else {
            OrbitalWolfCannonMod.LOGGER.warn("Orbital Wolf Rod recipe {} was not present in recipe manager; recipe book unlock skipped.", recipeId);
        }
    }
}

