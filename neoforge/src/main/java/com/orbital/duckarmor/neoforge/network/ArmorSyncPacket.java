package com.orbital.duckarmor.neoforge.network;

import com.orbital.duckarmor.platform.EntityDataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ArmorSyncPacket(int entityId, String nbtKey, boolean applied) {

    public static void encode(ArmorSyncPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.entityId);
        buf.writeUtf(pkt.nbtKey);
        buf.writeBoolean(pkt.applied);
    }

    public static ArmorSyncPacket decode(FriendlyByteBuf buf) {
        return new ArmorSyncPacket(buf.readInt(), buf.readUtf(), buf.readBoolean());
    }

    public static void handle(ArmorSyncPacket pkt, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) return;
            Entity entity = level.getEntity(pkt.entityId);
            if (!(entity instanceof LivingEntity living)) return;
            if (pkt.applied) {
                EntityDataHelper.putBoolean(living, pkt.nbtKey, true);
            } else {
                EntityDataHelper.remove(living, pkt.nbtKey);
            }
        });
        ctx.setPacketHandled(true);
    }
}
