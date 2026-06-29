package com.orbital.duckarmor.platform;

import com.orbital.duckarmor.forge.events.ForgeEvents;
import net.minecraftforge.common.MinecraftForge;

public class ServicesImpl {

    public static void registerGameEvents() {
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onLivingHurt);
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onPlayerInteractEntity);
    }

    public static void registerClientEvents() {
    }
}
