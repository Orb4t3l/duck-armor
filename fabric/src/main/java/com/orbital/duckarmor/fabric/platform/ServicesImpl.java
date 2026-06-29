package com.orbital.duckarmor.fabric.platform;

import com.orbital.duckarmor.fabric.events.FabricEvents;
import com.orbital.duckarmor.init.ModCreativeTabs;
import com.orbital.duckarmor.init.ModItems;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.entity.LivingEntity;

public class ServicesImpl {

    public static void registerGameEvents() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof LivingEntity living) {
                return FabricEvents.onShearEntity(player, living);
            }
            return net.minecraft.world.InteractionResult.PASS;
        });
    }

    public static void registerClientEvents() {
    }

    public static void populateCreativeTab() {
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, entries) -> {
            if (tab == ModCreativeTabs.DUCK_ARMOR_TAB) {
                entries.accept(ModItems.DUCK_ARMOR.get());
                entries.accept(ModItems.GOOSE_ARMOR.get());
            }
        });
    }
}