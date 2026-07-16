package com.orbital.duckarmor.platform;

import net.minecraft.world.entity.LivingEntity;

public class Services {
    public static final EntityDataProvider PLATFORM = load();

    private static EntityDataProvider load() {
        try {
            // Load NeoForge impl at runtime
            Class<?> clazz = Class.forName("com.orbital.duckarmor.neoforge.impl.NeoForgeEntityDataProvider");
            return (EntityDataProvider) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load EntityDataProvider", e);
        }
    }
}