package com.orbital.duckarmor.platform;

import net.minecraft.world.entity.LivingEntity;

public class EntityDataHelper {

    private static EntityDataProvider provider;

    public static void register(EntityDataProvider p) {
        provider = p;
    }

    public static boolean getBoolean(LivingEntity entity, String key) {
        return provider.getBoolean(entity, key);
    }

    public static void putBoolean(LivingEntity entity, String key, boolean value) {
        provider.putBoolean(entity, key, value);
    }

    public static void remove(LivingEntity entity, String key) {
        provider.remove(entity, key);
    }
}