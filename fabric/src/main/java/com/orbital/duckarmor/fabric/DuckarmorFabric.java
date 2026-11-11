package com.orbital.duckarmor.fabric;

import com.orbital.duckarmor.DuckarmorCommon;
import com.orbital.duckarmor.fabric.data.FabricEntityDataProvider;
import com.orbital.duckarmor.fabric.events.FabricEvents;
import com.orbital.duckarmor.fabric.network.FabricArmorSync;
import com.orbital.duckarmor.init.ModCreativeTabs;
import com.orbital.duckarmor.init.ModItems;
import com.orbital.duckarmor.item.DuckArmorItem;
import com.orbital.duckarmor.platform.EntityDataHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayDeque;
import java.util.Deque;

public class DuckarmorFabric implements ModInitializer {

    private static final Deque<Runnable> pendingSync = new ArrayDeque<>();
    private static final Deque<Runnable> pendingSyncNext = new ArrayDeque<>();

    @Override
    public void onInitialize() {
        EntityDataHelper.register(new FabricEntityDataProvider());

        DuckArmorItem.syncCallback = FabricArmorSync::sendUpdate;

        DuckArmorItem duckArmor = new DuckArmorItem(
                DuckArmorItem.DUCK_ARMOR_NBT, DuckArmorItem.DUCK_ENTITY_ID,
                new Item.Properties().stacksTo(1));
        DuckArmorItem gooseArmor = new DuckArmorItem(
                DuckArmorItem.GOOSE_ARMOR_NBT, DuckArmorItem.GOOSE_ENTITY_ID,
                new Item.Properties().stacksTo(1));

        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (entity instanceof LivingEntity living) {
                DuckArmorItem.reapplyIfNeeded(living);
            }
        });

        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
            if (trackedEntity instanceof LivingEntity living) {
                pendingSyncNext.add(() -> FabricArmorSync.syncToPlayer(player, living));
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            while (!pendingSync.isEmpty()) {
                pendingSync.poll().run();
            }
            pendingSync.addAll(pendingSyncNext);
            pendingSyncNext.clear();
        });

        Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation(DuckarmorCommon.MODID, "duck_armor"), duckArmor);
        Registry.register(BuiltInRegistries.ITEM,
                new ResourceLocation(DuckarmorCommon.MODID, "goose_armor"), gooseArmor);

        ModItems.DUCK_ARMOR = () -> duckArmor;
        ModItems.GOOSE_ARMOR = () -> gooseArmor;

        CreativeModeTab tab = FabricItemGroup.builder()
                .title(Component.translatable("itemGroup.duckarmor.duck_armor_tab"))
                .icon(() -> new ItemStack(duckArmor))
                .displayItems((params, output) -> {
                    output.accept(duckArmor);
                    output.accept(gooseArmor);
                })
                .build();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                new ResourceLocation(DuckarmorCommon.MODID, "duck_armor_tab"), tab);

        ModCreativeTabs.DUCK_ARMOR_TAB = tab;

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof LivingEntity living) {
                return FabricEvents.onShearEntity(player, living);
            }
            return net.minecraft.world.InteractionResult.PASS;
        });
    }
}