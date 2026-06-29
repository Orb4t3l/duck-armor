package com.orbital.duckarmor.platform;

import net.minecraft.world.entity.LivingEntity;

public class EntityDataHelperImpl {

    public static boolean getBoolean(LivingEntity entity, String key) {
        return entity.getPersistentData().getBoolean(key);
    }

    public static void putBoolean(LivingEntity entity, String key, boolean value) {
        entity.getPersistentData().putBoolean(key, value);
    }

    public static void remove(LivingEntity entity, String key) {
        entity.getPersistentData().remove(key);
    }
}
