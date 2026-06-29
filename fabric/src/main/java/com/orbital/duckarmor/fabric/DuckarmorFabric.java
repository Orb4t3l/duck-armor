package com.orbital.duckarmor.fabric;

import com.orbital.duckarmor.DuckarmorCommon;
import com.orbital.duckarmor.fabric.data.FabricEntityDataProvider;
import com.orbital.duckarmor.fabric.events.FabricEvents;
import com.orbital.duckarmor.init.ModCreativeTabs;
import com.orbital.duckarmor.init.ModItems;
import com.orbital.duckarmor.platform.EntityDataHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.entity.LivingEntity;

public class DuckarmorFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        EntityDataHelper.register(new FabricEntityDataProvider());
        
        // Register DeferredRegister (Fabric handles event bus automatically)
        ModItems.ITEMS.register();
        
        DuckarmorCommon.init();

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof LivingEntity living) {
                return FabricEvents.onShearEntity(player, living);
            }
            return net.minecraft.world.InteractionResult.PASS;
        });

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, entries) -> {
            if (tab == ModCreativeTabs.DUCK_ARMOR_TAB) {
                entries.accept(ModItems.DUCK_ARMOR.get());
                entries.accept(ModItems.GOOSE_ARMOR.get());
            }
        });
    }
}