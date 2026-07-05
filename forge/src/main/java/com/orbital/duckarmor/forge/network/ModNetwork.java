package com.orbital.duckarmor.forge.network;

import com.orbital.duckarmor.DuckarmorCommon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

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
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;
        CHANNEL.send(
                PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                new ArmorSyncPacket(entity.getId(), nbtKey, applied)
        );
    }
}
