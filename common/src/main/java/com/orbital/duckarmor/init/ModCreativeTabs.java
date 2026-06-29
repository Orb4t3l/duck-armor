package com.orbital.duckarmor.init;

import com.orbital.duckarmor.DuckarmorCommon;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {

    public static RegistrySupplier<CreativeModeTab> DUCK_ARMOR_TAB;

    public static void init() {
        DUCK_ARMOR_TAB = CreativeTabRegistry.create(
                new ResourceLocation(DuckarmorCommon.MODID, "duck_armor_tab"),
                () -> new ItemStack(ModItems.DUCK_ARMOR.get())
        );
        CreativeTabRegistry.append(DUCK_ARMOR_TAB, ModItems.DUCK_ARMOR, ModItems.GOOSE_ARMOR);
    }
}
