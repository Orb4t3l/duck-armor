package com.orbital.duckarmor.forge;

import com.orbital.duckarmor.Duckarmor;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Duckarmor.MOD_ID)
public final class DuckarmorForge {
    public DuckarmorForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(Duckarmor.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        Duckarmor.init();
    }
}
