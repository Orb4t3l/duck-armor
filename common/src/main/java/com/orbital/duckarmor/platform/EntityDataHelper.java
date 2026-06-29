package com.orbital.duckarmor.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.LivingEntity;

public class EntityDataHelper {

    @ExpectPlatform
    public static boolean getBoolean(LivingEntity entity, String key) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void putBoolean(LivingEntity entity, String key, boolean value) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void remove(LivingEntity entity, String key) {
        throw new AssertionError();
    }
}
