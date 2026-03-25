package com.slimeeystudios.orbitalwolfcannon.item;

import com.slimeeystudios.orbitalwolfcannon.entity.WolfStrikeManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class OrbitalWolfRodItem extends Item {
    public static final int COOLDOWN_TICKS = 20 * 60;

    public OrbitalWolfRodItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        if (!(user instanceof ServerPlayerEntity serverPlayer)) {
            return ActionResult.FAIL;
        }

        if (serverPlayer.getItemCooldownManager().isCoolingDown(stack)) {
            serverPlayer.sendMessage(Text.translatable("item.orbital_wolf_cannon.orbital_wolf_rod.cooldown"), true);
            return ActionResult.FAIL;
        }

        serverPlayer.getItemCooldownManager().set(stack, COOLDOWN_TICKS);
        WolfStrikeManager.executeStrike(serverPlayer);

        return ActionResult.SUCCESS;
    }
}
