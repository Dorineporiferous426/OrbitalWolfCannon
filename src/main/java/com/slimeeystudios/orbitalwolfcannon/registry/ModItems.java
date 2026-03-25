package com.slimeeystudios.orbitalwolfcannon.registry;

import com.slimeeystudios.orbitalwolfcannon.OrbitalWolfCannonMod;
import com.slimeeystudios.orbitalwolfcannon.item.OrbitalWolfRodItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModItems {
    private static final Identifier ORBITAL_WOLF_ROD_ID = Identifier.of(OrbitalWolfCannonMod.MOD_ID, "orbital_wolf_rod");

    public static final Item ORBITAL_WOLF_ROD = Registry.register(
            Registries.ITEM,
            ORBITAL_WOLF_ROD_ID,
            new OrbitalWolfRodItem(
                    new Item.Settings()
                            .registryKey(RegistryKey.of(RegistryKeys.ITEM, ORBITAL_WOLF_ROD_ID))
                            .maxCount(1)
            )
    );

    private ModItems() {
    }

    public static void register() {
        // Classload registration holder.
    }
}

