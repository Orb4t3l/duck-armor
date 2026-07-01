package com.orbital.duckarmor.fabric;

import com.orbital.duckarmor.DuckarmorCommon;
import com.orbital.duckarmor.fabric.data.FabricEntityDataProvider;
import com.orbital.duckarmor.fabric.events.FabricEvents;
import com.orbital.duckarmor.init.ModItems;
import com.orbital.duckarmor.item.DuckArmorItem;
import com.orbital.duckarmor.platform.EntityDataHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public class DuckarmorFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        EntityDataHelper.register(new FabricEntityDataProvider());

        DuckArmorItem duckArmor = new DuckArmorItem(
                DuckArmorItem.DUCK_ARMOR_NBT, DuckArmorItem.DUCK_ENTITY_ID,
                new Item.Properties().stacksTo(1));
        DuckArmorItem gooseArmor = new DuckArmorItem(
                DuckArmorItem.GOOSE_ARMOR_NBT, DuckArmorItem.GOOSE_ENTITY_ID,
                new Item.Properties().stacksTo(1));

        Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation(DuckarmorCommon.MODID, "duck_armor"), duckArmor);
        Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation(DuckarmorCommon.MODID, "goose_armor"), gooseArmor);

        ModItems.DUCK_ARMOR = () -> duckArmor;
        ModItems.GOOSE_ARMOR = () -> gooseArmor;

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof LivingEntity living) {
                return FabricEvents.onShearEntity(player, living);
            }
            return net.minecraft.world.InteractionResult.PASS;
        });

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, entries) -> {
            if (tab.getDisplayName().getString().equals("Duck Armor")) {
                entries.accept(duckArmor);
                entries.accept(gooseArmor);
            }
        });
    }
}
