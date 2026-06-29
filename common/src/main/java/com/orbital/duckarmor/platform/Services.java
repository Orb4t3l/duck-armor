package com.orbital.duckarmor.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class Services {

    @ExpectPlatform
    public static void registerGameEvents() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerClientEvents() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void populateCreativeTab() {
        throw new AssertionError();
    }
}