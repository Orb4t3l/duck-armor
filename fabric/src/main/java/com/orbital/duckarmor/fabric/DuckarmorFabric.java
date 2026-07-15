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
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DuckarmorFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        EntityDataHelper.register(new FabricEntityDataProvider());

        // Must be registered on both physical sides — this runs on both
        // the client (integrated server + client) and dedicated server.
        FabricArmorSync.registerPayloadType();
        DuckArmorItem.syncCallback = FabricArmorSync::sendUpdate;

        DuckArmorItem duckArmor = new DuckArmorItem(
                DuckArmorItem.DUCK_ARMOR_NBT, DuckArmorItem.DUCK_ENTITY_ID,
                new Item.Properties().stacksTo(1));
        DuckArmorItem gooseArmor = new DuckArmorItem(
                DuckArmorItem.GOOSE_ARMOR_NBT, DuckArmorItem.GOOSE_ENTITY_ID,
                new Item.Properties().stacksTo(1));

        Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.parse(DuckarmorCommon.MODID + ":duck_armor"), duckArmor);
        Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.parse(DuckarmorCommon.MODID + ":goose_armor"), gooseArmor);

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
                ResourceLocation.parse(DuckarmorCommon.MODID + ":duck_armor_tab"), tab);

        ModCreativeTabs.DUCK_ARMOR_TAB = tab;

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof LivingEntity living) {
                return FabricEvents.onShearEntity(player, living);
            }
            return net.minecraft.world.InteractionResult.PASS;
        });
    }
}
