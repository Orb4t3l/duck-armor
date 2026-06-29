package com.orbital.duckarmor.fabric;

import com.orbital.duckarmor.DuckarmorCommon;
import net.fabricmc.api.ModInitializer;

public class DuckarmorFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        DuckarmorCommon.init();
    }
}
