package com.orbital.duckarmor.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class Services {

    @ExpectPlatform
    public static void registerGameEvents() {
        throw new AssertionError();
    }

    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    public static void registerClientEvents() {
        throw new AssertionError();
    }
}
