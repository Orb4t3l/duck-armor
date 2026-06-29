package com.orbital.duckarmor.forge.platform;

import com.orbital.duckarmor.platform.EntityDataProvider;
import net.minecraft.world.entity.LivingEntity;

public class ForgeEntityDataProvider implements EntityDataProvider {

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