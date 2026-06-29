package com.orbital.duckarmor.platform;

import net.minecraft.world.entity.LivingEntity;

public interface EntityDataProvider {
    boolean getBoolean(LivingEntity entity, String key);
    void putBoolean(LivingEntity entity, String key, boolean value);
    void remove(LivingEntity entity, String key);
}