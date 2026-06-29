package com.orbital.duckarmor.init;

import dev.architectury.registry.CreativeTabRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {

    public static CreativeModeTab DUCK_ARMOR_TAB;

    public static void init() {
        DUCK_ARMOR_TAB = CreativeTabRegistry.create(
                Component.translatable("itemGroup.duckarmor.duck_armor_tab"),
                () -> new ItemStack(ModItems.DUCK_ARMOR.get())
        );
    }
}