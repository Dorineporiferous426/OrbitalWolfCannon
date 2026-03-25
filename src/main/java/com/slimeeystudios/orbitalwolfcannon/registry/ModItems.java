package com.slimeeystudios.orbitalwolfcannon.registry;

import com.slimeeystudios.orbitalwolfcannon.OrbitalWolfCannonMod;
import com.slimeeystudios.orbitalwolfcannon.item.OrbitalWolfRodItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModItems {
    public static final Item ORBITAL_WOLF_ROD = Registry.register(
            Registries.ITEM,
            Identifier.of(OrbitalWolfCannonMod.MOD_ID, "orbital_wolf_rod"),
            new OrbitalWolfRodItem(new Item.Settings().maxCount(1))
    );

    private ModItems() {
    }

    public static void register() {
        // Classload registration holder.
    }
}

