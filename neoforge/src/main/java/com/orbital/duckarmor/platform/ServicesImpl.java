package com.orbital.duckarmor.platform;

import com.orbital.duckarmor.neoforge.events.NeoForgeEvents;
import net.minecraftforge.common.MinecraftForge;

public class ServicesImpl {

    public static void registerGameEvents() {
        MinecraftForge.EVENT_BUS.addListener(NeoForgeEvents::onLivingHurt);
        MinecraftForge.EVENT_BUS.addListener(NeoForgeEvents::onPlayerInteractEntity);
    }

    public static void registerClientEvents() {
    }
}
