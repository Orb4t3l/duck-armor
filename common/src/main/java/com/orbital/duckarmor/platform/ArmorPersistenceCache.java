package com.orbital.duckarmor.platform;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

    public static Set<String> getAll(UUID entityUuid) {
        return ARMOR_BY_UUID.getOrDefault(entityUuid, Set.of());
    }
}