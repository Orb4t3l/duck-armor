package com.orbital.duckarmor.fabric.network;

import com.orbital.duckarmor.item.DuckArmorItem;
import com.orbital.duckarmor.platform.EntityDataHelper;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FabricArmorSync {

    public static void registerPayloadTypes() {
        PayloadTypeRegistry.playS2C().register(ArmorSyncPayload.TYPE, ArmorSyncPayload.STREAM_CODEC);
    }

    public static void sendUpdate(LivingEntity entity, String nbtKey, boolean applied) {
        Level level = entity.level();
        if (level.isClientSide()) return;
        ArmorSyncPayload payload = new ArmorSyncPayload(entity.getId(), nbtKey, applied);
        for (ServerPlayer player : ((net.minecraft.server.level.ServerLevel) level).players()) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    public static void syncToPlayer(ServerPlayer player, LivingEntity entity) {
        if (DuckArmorItem.hasDuckArmor(entity)) {
            ServerPlayNetworking.send(player,
                    new ArmorSyncPayload(entity.getId(), DuckArmorItem.DUCK_ARMOR_NBT, true));
        }
        if (DuckArmorItem.hasGooseArmor(entity)) {
            ServerPlayNetworking.send(player,
                    new ArmorSyncPayload(entity.getId(), DuckArmorItem.GOOSE_ARMOR_NBT, true));
        }
    }

    public static void handleClientReceive(Minecraft client, ArmorSyncPayload payload) {
        if (client.level == null) return;
        Entity entity = client.level.getEntity(payload.entityId());
        if (!(entity instanceof LivingEntity living)) return;
        if (payload.applied()) {
            EntityDataHelper.putBoolean(living, payload.nbtKey(), true);
        } else {
            EntityDataHelper.remove(living, payload.nbtKey());
        }
    }
}