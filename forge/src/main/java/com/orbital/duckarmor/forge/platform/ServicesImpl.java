package com.orbital.duckarmor.forge.platform;

import com.orbital.duckarmor.forge.events.ForgeEvents;
import com.orbital.duckarmor.init.ModCreativeTabs;
import com.orbital.duckarmor.init.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

public class ServicesImpl {

    public static void registerGameEvents() {
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onLivingHurt);
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onPlayerInteractEntity);
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