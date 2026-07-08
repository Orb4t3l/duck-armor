package com.orbital.duckarmor.platform;

import net.minecraft.world.entity.LivingEntity;

public class NeoForgeEntityDataProvider implements EntityDataProvider {

    @Override
    public boolean getBoolean(LivingEntity entity, String key) {
        return entity.getPersistentData().getBoolean(key);
    }

    @Override
    public void putBoolean(LivingEntity entity, String key, boolean value) {
        entity.getPersistentData().putBoolean(key, value);
    }

    @Override
    public void remove(LivingEntity entity, String key) {
        entity.getPersistentData().remove(key);
    }
}
