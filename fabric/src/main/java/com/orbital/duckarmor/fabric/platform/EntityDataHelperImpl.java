package com.orbital.duckarmor.fabric.platform;

import com.orbital.duckarmor.fabric.data.DuckarmorEntityData;
import net.minecraft.world.entity.LivingEntity;

public class EntityDataHelperImpl {

    public static boolean getBoolean(LivingEntity entity, String key) {
        return ((DuckarmorEntityData) entity).duckarmor$getData().getBoolean(key);
    }

    public static void putBoolean(LivingEntity entity, String key, boolean value) {
        ((DuckarmorEntityData) entity).duckarmor$getData().putBoolean(key, value);
    }

    public static void remove(LivingEntity entity, String key) {
        ((DuckarmorEntityData) entity).duckarmor$getData().remove(key);
    }
}
