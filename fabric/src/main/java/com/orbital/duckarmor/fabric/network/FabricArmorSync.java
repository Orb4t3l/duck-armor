package com.orbital.duckarmor.fabric.network;

import com.orbital.duckarmor.platform.EntityDataHelper;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * Even in singleplayer, the client's rendering entity and the integrated
 * server's simulation entity are separate Java objects connected only by
 * network packets. Our mixin's armor flag lives on the entity instance
 * itself, so writing it server-side never reaches the client's copy without
 * an explicit packet — this is that packet, mirroring what ModNetwork does
 * on Forge.
 */
public class FabricArmorSync {

    public static final ResourceLocation CHANNEL = new ResourceLocation("duckarmor", "armor_sync");

    /** Called from the server when armor is applied or removed. */
    public static void sendUpdate(LivingEntity entity, String nbtKey, boolean applied) {
        if (!(entity.level() instanceof net.minecraft.server.level.ServerLevel)) return;

        FriendlyByteBuf buf = new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
        buf.writeInt(entity.getId());
        buf.writeUtf(nbtKey);
        buf.writeBoolean(applied);

        for (ServerPlayer player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.send(player, CHANNEL, buf);
        }
    }

    /** Called on the client when a sync packet arrives. */
    public static void handleClientReceive(net.minecraft.client.Minecraft client, FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        String nbtKey = buf.readUtf();
        boolean applied = buf.readBoolean();

        client.execute(() -> {
            if (client.level == null) return;
            Entity entity = client.level.getEntity(entityId);
            if (!(entity instanceof LivingEntity living)) return;

            if (applied) {
                EntityDataHelper.putBoolean(living, nbtKey, true);
            } else {
                EntityDataHelper.remove(living, nbtKey);
            }
        });
    }
}