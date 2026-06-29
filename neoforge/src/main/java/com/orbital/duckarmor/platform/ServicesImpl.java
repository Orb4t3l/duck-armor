package com.orbital.duckarmor.platform;

import com.orbital.duckarmor.init.ModCreativeTabs;
import com.orbital.duckarmor.init.ModItems;
import com.orbital.duckarmor.neoforge.events.NeoForgeEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

public class ServicesImpl {

    public static void registerGameEvents() {
        MinecraftForge.EVENT_BUS.addListener(NeoForgeEvents::onLivingHurt);
        MinecraftForge.EVENT_BUS.addListener(NeoForgeEvents::onPlayerInteractEntity);
    }

    public static void registerClientEvents() {
    }

    public static void populateCreativeTab() {
        MinecraftForge.EVENT_BUS.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if (event.getTab() == ModCreativeTabs.DUCK_ARMOR_TAB) {
                event.accept(new ItemStack(ModItems.DUCK_ARMOR.get()));
                event.accept(new ItemStack(ModItems.GOOSE_ARMOR.get()));
            }
        });
    }
}