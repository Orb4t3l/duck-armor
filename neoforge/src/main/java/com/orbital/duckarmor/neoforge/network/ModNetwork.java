package com.orbital.duckarmor.neoforge.network;

import com.orbital.duckarmor.DuckarmorCommon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class ModNetwork {

    public static void register(RegisterPayloadHandlersEvent event) {
        final var registrar = event.registrar("1");
        registrar.playToClient(ArmorSyncPacket.TYPE, ArmorSyncPacket.STREAM_CODEC, ArmorSyncPacket::handle);
    }

    public static void sendArmorUpdate(LivingEntity entity, String nbtKey, boolean applied) {
        if (!(entity.level() instanceof ServerLevel)) return;
        PacketDistributor.sendToPlayersTrackingEntity(entity, new ArmorSyncPacket(entity.getId(), nbtKey, applied));
    }
}
