package com.orbital.duckarmor.fabric.network;

import com.orbital.duckarmor.item.DuckArmorItem;
import com.orbital.duckarmor.platform.EntityDataHelper;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FabricArmorSync {

    private static final Logger LOGGER = LogManager.getLogger("duckarmor");

    public static final ResourceLocation CHANNEL = new ResourceLocation("duckarmor", "armor_sync");

    public static void sendUpdate(LivingEntity entity, String nbtKey, boolean applied) {
        Level level = entity.level();
        if (level.isClientSide()) return;
        for (ServerPlayer player : ((net.minecraft.server.level.ServerLevel) level).players()) {
            sendTo(player, entity.getId(), nbtKey, applied);
        }
    }

    public static void syncToPlayer(ServerPlayer player, LivingEntity entity) {
        LOGGER.info("DuckArmor: syncToPlayer called for entity {} duck={} goose={}",
                entity.getId(), DuckArmorItem.hasDuckArmor(entity), DuckArmorItem.hasGooseArmor(entity));
        if (DuckArmorItem.hasDuckArmor(entity)) {
            sendTo(player, entity.getId(), DuckArmorItem.DUCK_ARMOR_NBT, true);
        }
        if (DuckArmorItem.hasGooseArmor(entity)) {
            sendTo(player, entity.getId(), DuckArmorItem.GOOSE_ARMOR_NBT, true);
        }
    }

    private static void sendTo(ServerPlayer player, int entityId, String nbtKey, boolean applied) {
        LOGGER.info("DuckArmor: sendTo called, entityId={} nbtKey={} applied={}", entityId, nbtKey, applied);
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityId);
        buf.writeUtf(nbtKey);
        buf.writeBoolean(applied);
        ServerPlayNetworking.send(player, CHANNEL, buf);
    }

    public static void handleClientReceive(Minecraft client, FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        String nbtKey = buf.readUtf();
        boolean applied = buf.readBoolean();
        client.execute(() -> {
            if (client.level == null) return;
            Entity entity = client.level.getEntity(entityId);
            if (!(entity instanceof LivingEntity living)) {
                LOGGER.info("DuckArmor: client got sync for entityId={} but entity not found/not living", entityId);
                return;
            }
            if (applied) {
                EntityDataHelper.putBoolean(living, nbtKey, true);
            } else {
                EntityDataHelper.remove(living, nbtKey);
            }
            LOGGER.info("DuckArmor: client applied sync entityId={} nbtKey={} applied={}", entityId, nbtKey, applied);
        });
    }
}