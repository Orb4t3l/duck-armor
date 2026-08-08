package com.orbital.duckarmor.fabric.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ArmorSyncPayload(int entityId, String nbtKey, boolean applied) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ArmorSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(new ResourceLocation("duckarmor", "armor_sync"));

    public static final StreamCodec<FriendlyByteBuf, ArmorSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ArmorSyncPayload::entityId,
            ByteBufCodecs.STRING_UTF8, ArmorSyncPayload::nbtKey,
            ByteBufCodecs.BOOL, ArmorSyncPayload::applied,
            ArmorSyncPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}