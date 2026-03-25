package com.slimeeystudios.orbitalwolfcannon;

import com.slimeeystudios.orbitalwolfcannon.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class OrbitalWolfCannonClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.isOf(ModItems.ORBITAL_WOLF_ROD)) {
                lines.add(Text.translatable("item.orbital_wolf_cannon.orbital_wolf_rod.tooltip").formatted(Formatting.GRAY));
            }
        });
    }
}

