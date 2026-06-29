package com.orbital.duckarmor.neoforge;

import com.orbital.duckarmor.DuckarmorCommon;
import com.orbital.duckarmor.neoforge.client.NeoForgeClientEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(DuckarmorCommon.MODID)
public class DuckarmorNeoForge {

    public DuckarmorNeoForge() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        DuckarmorCommon.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modBus.addListener(NeoForgeClientEvents::onAddEntityRenderLayers);
        }
    }
}
