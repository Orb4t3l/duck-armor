package com.orbital.duckarmor.fabric.data;

import com.orbital.duckarmor.platform.EntityDataProvider;
import net.minecraft.world.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FabricEntityDataProvider implements EntityDataProvider {

    private static final Logger LOGGER = LogManager.getLogger("duckarmor");

    @Override
    public boolean getBoolean(LivingEntity entity, String key) {
        try {
            boolean value = ((DuckarmorEntityData) entity).duckarmor$getData().getBoolean(key);
            return value;
        } catch (ClassCastException e) {
            LOGGER.error("DuckArmor: entity {} does NOT implement DuckarmorEntityData — mixin not applied!",
                    entity.getClass().getName(), e);
            return false;
        }
    }

    @Override
    public void putBoolean(LivingEntity entity, String key, boolean value) {
        try {
            ((DuckarmorEntityData) entity).duckarmor$getData().putBoolean(key, value);
            LOGGER.info("DuckArmor: putBoolean succeeded for key={} value={}", key, value);
        } catch (ClassCastException e) {
            LOGGER.error("DuckArmor: entity {} does NOT implement DuckarmorEntityData — mixin not applied!",
                    entity.getClass().getName(), e);
        }
    }

    @Override
    public void remove(LivingEntity entity, String key) {
        ((DuckarmorEntityData) entity).duckarmor$getData().remove(key);
    }
}