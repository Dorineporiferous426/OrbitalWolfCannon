package com.slimeeystudios.orbitalwolfcannon.entity;

import com.slimeeystudios.orbitalwolfcannon.OrbitalWolfCannonMod;
import com.slimeeystudios.orbitalwolfcannon.config.OrbitalWolfCannonConfig;
import com.slimeeystudios.orbitalwolfcannon.util.CirclePositionUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class WolfStrikeManager {

    private static final Map<UUID, Long> TRACKED_WOLVES = new HashMap<>();

    private WolfStrikeManager() {
    }

    public static void registerTickHandler() {
        ServerTickEvents.END_SERVER_TICK.register(WolfStrikeManager::onServerTick);
    }

    public static void executeStrike(ServerPlayerEntity player) {
        ServerWorld world = (ServerWorld) player.getEntityWorld();

        spawnActivationRing(world, player);
        world.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ENTITY_ENDER_DRAGON_GROWL,
                SoundCategory.PLAYERS,
                OrbitalWolfCannonConfig.summonSoundVolume,
                OrbitalWolfCannonConfig.summonSoundPitch
        );

        List<double[]> positions = CirclePositionUtil.evenlyDistributedXZ(
                player.getX(),
                player.getZ(),
                OrbitalWolfCannonConfig.circleRadius,
                OrbitalWolfCannonConfig.wolfCount
        );

        for (double[] position : positions) {
            summonSingleWolf(world, player, position[0], player.getY(), position[1]);
        }
    }

    private static void summonSingleWolf(ServerWorld world, ServerPlayerEntity owner, double x, double y, double z) {
        WolfEntity wolf = EntityType.WOLF.create(world, SpawnReason.EVENT);
        if (wolf == null) {
            OrbitalWolfCannonMod.LOGGER.warn("Failed to create wolf entity during Orbital Wolf Rod activation.");
            return;
        }

        wolf.refreshPositionAndAngles(x, y, z, owner.getYaw(), 0.0F);
        wolf.setOwner(owner);
        wolf.setTamed(true, true);
        wolf.setSitting(false);
        wolf.equipStack(EquipmentSlot.BODY, new ItemStack(Items.WOLF_ARMOR));
        wolf.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, OrbitalWolfCannonConfig.strengthDurationTicks, OrbitalWolfCannonConfig.strengthAmplifier));

        world.spawnEntity(wolf);
        retargetToNearbyHostile(wolf);

        world.spawnParticles(ParticleTypes.EXPLOSION, x, y + 0.2D, z, 2, 0.15D, 0.05D, 0.15D, 0.01D);
        world.spawnParticles(ParticleTypes.CLOUD, x, y + 0.1D, z, 5, 0.2D, 0.06D, 0.2D, 0.01D);

        if (OrbitalWolfCannonConfig.autoDespawnEnabled) {
            TRACKED_WOLVES.put(wolf.getUuid(), world.getTime() + OrbitalWolfCannonConfig.wolfDespawnTicks);
        }
    }

    private static void spawnActivationRing(ServerWorld world, ServerPlayerEntity player) {
        List<double[]> ring = CirclePositionUtil.evenlyDistributedXZ(player.getX(), player.getZ(), OrbitalWolfCannonConfig.circleRadius, 72);
        double y = player.getY() + 0.05D;

        for (double[] position : ring) {
            world.spawnParticles(ParticleTypes.CLOUD, position[0], y, position[1], 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    private static void retargetToNearbyHostile(WolfEntity wolf) {
        Box scanBox = wolf.getBoundingBox().expand(16.0D);
        List<HostileEntity> hostiles = wolf.getEntityWorld().getEntitiesByClass(HostileEntity.class, scanBox, LivingEntity::isAlive);
        if (!hostiles.isEmpty()) {
            wolf.setTarget(hostiles.get(0));
        }
    }

    private static void onServerTick(MinecraftServer server) {
        if (TRACKED_WOLVES.isEmpty()) {
            return;
        }

        for (ServerWorld world : server.getWorlds()) {
            cleanupAndRetarget(world);
        }
    }

    private static void cleanupAndRetarget(ServerWorld world) {
        long worldTime = world.getTime();
        Iterator<Map.Entry<UUID, Long>> iterator = TRACKED_WOLVES.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Long> entry = iterator.next();
            Entity entity = world.getEntity(entry.getKey());

            if (!(entity instanceof WolfEntity wolf) || !wolf.isAlive()) {
                iterator.remove();
                continue;
            }

            if (OrbitalWolfCannonConfig.autoDespawnEnabled && worldTime >= entry.getValue()) {
                wolf.discard();
                iterator.remove();
                continue;
            }

            if (worldTime % 20L == 0L && (wolf.getTarget() == null || !wolf.getTarget().isAlive())) {
                retargetToNearbyHostile(wolf);
            }
        }
    }
}

