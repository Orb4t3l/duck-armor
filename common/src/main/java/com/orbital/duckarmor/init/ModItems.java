package com.orbital.duckarmor.init;

import com.orbital.duckarmor.DuckarmorCommon;
import com.orbital.duckarmor.item.DuckArmorItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(DuckarmorCommon.MODID, Registries.ITEM);

    public static final RegistrySupplier<DuckArmorItem> DUCK_ARMOR = ITEMS.register("duck_armor",
            () -> new DuckArmorItem(
                    DuckArmorItem.DUCK_ARMOR_NBT,
                    DuckArmorItem.DUCK_ENTITY_ID,
                    new Item.Properties().stacksTo(1)));

    public static final RegistrySupplier<DuckArmorItem> GOOSE_ARMOR = ITEMS.register("goose_armor",
            () -> new DuckArmorItem(
                    DuckArmorItem.GOOSE_ARMOR_NBT,
                    DuckArmorItem.GOOSE_ENTITY_ID,
                    new Item.Properties().stacksTo(1)));

    public static void init() {
        ITEMS.register();
    }
}
