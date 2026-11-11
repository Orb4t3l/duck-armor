package com.orbital.duckarmor.neoforge.network;

import com.orbital.duckarmor.item.DuckArmorItem;
import com.orbital.duckarmor.DuckarmorCommon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.ArrayDeque;
import java.util.Deque;

public class ModNetwork {

    private static final Deque<Runnable> pendingSync = new ArrayDeque<>();
    private static final Deque<Runnable> pendingSyncNext = new ArrayDeque<>();

    public static void register(RegisterPayloadHandlersEvent event) {
        final var registrar = event.registrar("1");
        registrar.playToClient(ArmorSyncPacket.TYPE, ArmorSyncPacket.STREAM_CODEC, ArmorSyncPacket::handle);
    }

    public static void sendArmorUpdate(LivingEntity entity, String nbtKey, boolean applied) {
        if (!(entity.level() instanceof ServerLevel)) return;
        PacketDistributor.sendToPlayersTrackingEntity(entity, new ArmorSyncPacket(entity.getId(), nbtKey, applied));
    }

    public static void queueSyncToPlayer(ServerPlayer player, LivingEntity entity) {
        pendingSyncNext.add(() -> {
            if (DuckArmorItem.hasDuckArmor(entity)) {
                PacketDistributor.sendToPlayer(player, new ArmorSyncPacket(entity.getId(), DuckArmorItem.DUCK_ARMOR_NBT, true));
            }
            if (DuckArmorItem.hasGooseArmor(entity)) {
                PacketDistributor.sendToPlayer(player, new ArmorSyncPacket(entity.getId(), DuckArmorItem.GOOSE_ARMOR_NBT, true));
            }
        });
    }

    public static void onServerTick(ServerTickEvent.Post event) {
        while (!pendingSync.isEmpty()) {
            pendingSync.poll().run();
        }
        pendingSync.addAll(pendingSyncNext);
        pendingSyncNext.clear();
    }
}