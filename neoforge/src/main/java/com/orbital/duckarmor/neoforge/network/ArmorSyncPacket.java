package com.orbital.duckarmor.neoforge.network;

import com.orbital.duckarmor.DuckarmorCommon;
import com.orbital.duckarmor.platform.EntityDataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ArmorSyncPacket(int entityId, String nbtKey, boolean applied) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ArmorSyncPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DuckarmorCommon.MODID, "armor_sync"));

    public static final StreamCodec<FriendlyByteBuf, ArmorSyncPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ArmorSyncPacket::entityId,
            ByteBufCodecs.STRING_UTF8,
            ArmorSyncPacket::nbtKey,
            ByteBufCodecs.BOOL,
            ArmorSyncPacket::applied,
            ArmorSyncPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ArmorSyncPacket pkt, IPayloadContext ctx) {
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
    }
}
