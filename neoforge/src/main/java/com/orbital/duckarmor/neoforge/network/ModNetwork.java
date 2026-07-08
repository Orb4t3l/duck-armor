package com.orbital.duckarmor.neoforge.network;

import com.orbital.duckarmor.DuckarmorCommon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.NetworkDirection;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.simple.SimpleChannel;

public class ModNetwork {

    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(DuckarmorCommon.MODID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    public static void register() {
        CHANNEL.registerMessage(0, ArmorSyncPacket.class,
                ArmorSyncPacket::encode,
                ArmorSyncPacket::decode,
                ArmorSyncPacket::handle,
                java.util.Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendArmorUpdate(LivingEntity entity, String nbtKey, boolean applied) {
        if (!(entity.level() instanceof ServerLevel)) return;
        CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                new ArmorSyncPacket(entity.getId(), nbtKey, applied)
        );
    }
}
