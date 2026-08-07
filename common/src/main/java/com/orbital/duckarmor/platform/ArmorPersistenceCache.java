package com.orbital.duckarmor.platform;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Minecraft destroys and recreates the entity Java object when it changes
 * dimension (portal travel, /execute in, etc.) — only a partial set of
 * fields gets copied to the new instance, and our custom armor flag
 * (stored via getPersistentData() or a Fabric mixin field) is frequently
 * lost in that reconstruction even though the entity's UUID stays the same.
 *
 * This cache tracks armor state by UUID (stable across the reconstruction)
 * so each platform's "entity joined level" hook can detect a freshly
 * recreated duck/goose and reapply the flag immediately.
 */
public class ArmorPersistenceCache {

    private static final ConcurrentHashMap<UUID, Set<String>> ARMOR_BY_UUID = new ConcurrentHashMap<>();

    public static void register(UUID entityUuid, String nbtKey) {
        ARMOR_BY_UUID.computeIfAbsent(entityUuid, k -> ConcurrentHashMap.newKeySet()).add(nbtKey);
    }

    public static void unregister(UUID entityUuid, String nbtKey) {
        Set<String> keys = ARMOR_BY_UUID.get(entityUuid);
        if (keys != null) {
            keys.remove(nbtKey);
            if (keys.isEmpty()) ARMOR_BY_UUID.remove(entityUuid);
        }
    }

    public static boolean has(UUID entityUuid, String nbtKey) {
        Set<String> keys = ARMOR_BY_UUID.get(entityUuid);
        return keys != null && keys.contains(nbtKey);
    }

    public static Set<String> getAll(UUID entityUuid) {
        return ARMOR_BY_UUID.getOrDefault(entityUuid, Set.of());
    }
}