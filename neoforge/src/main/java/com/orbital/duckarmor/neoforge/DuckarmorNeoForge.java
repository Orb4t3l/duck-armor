package com.orbital.duckarmor.neoforge;

import com.orbital.duckarmor.DuckarmorCommon;
import com.orbital.duckarmor.init.ModItems;
import com.orbital.duckarmor.item.DuckArmorItem;
import com.orbital.duckarmor.neoforge.client.NeoForgeClientEvents;
import com.orbital.duckarmor.neoforge.events.NeoForgeEvents;
import com.orbital.duckarmor.neoforge.network.ModNetwork;
import com.orbital.duckarmor.platform.NeoForgeEntityDataProvider;
import com.orbital.duckarmor.platform.EntityDataHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

@Mod(DuckarmorCommon.MODID)
public class DuckarmorNeoForge {

    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.createItems(DuckarmorCommon.MODID);

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DuckarmorCommon.MODID);

    private static final DeferredHolder<Item, DuckArmorItem> DUCK_ARMOR = ITEMS.register("duck_armor",
            () -> new DuckArmorItem(DuckArmorItem.DUCK_ARMOR_NBT, DuckArmorItem.DUCK_ENTITY_ID,
                    new Item.Properties().stacksTo(1)));

    private static final DeferredHolder<Item, DuckArmorItem> GOOSE_ARMOR = ITEMS.register("goose_armor",
            () -> new DuckArmorItem(DuckArmorItem.GOOSE_ARMOR_NBT, DuckArmorItem.GOOSE_ENTITY_ID,
                    new Item.Properties().stacksTo(1)));

    private static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = TABS.register("duck_armor_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.duckarmor.duck_armor_tab"))
                    .icon(() -> new ItemStack(DUCK_ARMOR.get()))
                    .build());

    static {
        ModItems.DUCK_ARMOR = DUCK_ARMOR;
        ModItems.GOOSE_ARMOR = GOOSE_ARMOR;
    }

    public DuckarmorNeoForge() {
        IEventBus modBus = ModLoadingContext.get().getModEventBus();

        EntityDataHelper.register(new NeoForgeEntityDataProvider());

        modBus.addListener(ModNetwork::register);
        DuckArmorItem.syncCallback = ModNetwork::sendArmorUpdate;

        ITEMS.register(modBus);
        TABS.register(modBus);

        NeoForge.EVENT_BUS.addListener(NeoForgeEvents::onLivingDamage);
        NeoForge.EVENT_BUS.addListener(NeoForgeEvents::onPlayerInteractEntity);

        modBus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if (event.getTabKey().location().equals(
                    ResourceLocation.fromNamespaceAndPath(DuckarmorCommon.MODID, "duck_armor_tab"))) {
                event.accept(DUCK_ARMOR.get());
                event.accept(GOOSE_ARMOR.get());
            }
        });

        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.addListener(NeoForgeClientEvents::onClientTick);
        }
    }
}
