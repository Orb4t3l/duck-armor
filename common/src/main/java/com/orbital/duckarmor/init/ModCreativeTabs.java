package com.orbital.duckarmor.init;

import com.orbital.duckarmor.DuckarmorCommon;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(DuckarmorCommon.MODID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> DUCK_ARMOR_TAB = TABS.register(
            "duck_armor_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.duckarmor.tab"))
                    .icon(() -> new ItemStack(ModItems.DUCK_ARMOR.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.DUCK_ARMOR.get());
                        output.accept(ModItems.GOOSE_ARMOR.get());
                    })
                    .build());

    public static void init() {
        TABS.register();
    }
}
