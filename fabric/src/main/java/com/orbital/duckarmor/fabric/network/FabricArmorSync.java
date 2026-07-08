package com.orbital.duckarmor.fabric.network;

import com.orbital.duckarmor.platform.EntityDataHelper;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class FabricArmorSync {

    /** Must be called once, on BOTH client and server (i.e. from common init). */
    public static void registerPayloadType() {
        PayloadTypeRegistry.playS2C().register(ArmorSyncPayload.TYPE, ArmorSyncPayload.STREAM_CODEC);
    }

    /** Called from the server when armor is applied or removed. */
    public static void sendUpdate(LivingEntity entity, String nbtKey, boolean applied) {
        if (!(entity.level() instanceof net.minecraft.server.level.ServerLevel)) return;

        ArmorSyncPayload payload = new ArmorSyncPayload(entity.getId(), nbtKey, applied);

        for (ServerPlayer player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    /** Called on the client when a sync packet arrives. */
    public static void handleClientReceive(ArmorSyncPayload payload, net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context context) {
        Minecraft client = context.client();
        client.execute(() -> {
            if (client.level == null) return;
            Entity entity = client.level.getEntity(payload.entityId());
            if (!(entity instanceof LivingEntity living)) return;

            if (payload.applied()) {
                EntityDataHelper.putBoolean(living, payload.nbtKey(), true);
            } else {
                EntityDataHelper.remove(living, payload.nbtKey());
            }
        });
    }
}
