package com.orbital.duckarmor.fabric.data;

import com.orbital.duckarmor.platform.EntityDataProvider;
import net.minecraft.world.entity.LivingEntity;

public class FabricEntityDataProvider implements EntityDataProvider {

    @Override
    public boolean getBoolean(LivingEntity entity, String key) {
        return ((DuckarmorEntityData) entity).duckarmor$getData().getBoolean(key);
    }

    @Override
    public void putBoolean(LivingEntity entity, String key, boolean value) {
        ((DuckarmorEntityData) entity).duckarmor$getData().putBoolean(key, value);
    }

    @Override
    public void remove(LivingEntity entity, String key) {
        ((DuckarmorEntityData) entity).duckarmor$getData().remove(key);
    }
}