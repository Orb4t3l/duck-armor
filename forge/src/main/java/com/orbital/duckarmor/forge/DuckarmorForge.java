package com.orbital.duckarmor.forge;

import com.orbital.duckarmor.DuckarmorCommon;
import com.orbital.duckarmor.forge.client.ForgeClientEvents;
import com.orbital.duckarmor.forge.events.ForgeEvents;
import com.orbital.duckarmor.forge.platform.ForgeEntityDataProvider;
import com.orbital.duckarmor.init.ModItems;
import com.orbital.duckarmor.item.DuckArmorItem;
import com.orbital.duckarmor.platform.EntityDataHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(DuckarmorCommon.MODID)
public class DuckarmorForge {

    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DuckarmorCommon.MODID);

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DuckarmorCommon.MODID);

    private static final RegistryObject<DuckArmorItem> DUCK_ARMOR = ITEMS.register("duck_armor",
            () -> new DuckArmorItem(DuckArmorItem.DUCK_ARMOR_NBT, DuckArmorItem.DUCK_ENTITY_ID,
                    new Item.Properties().stacksTo(1)));

    private static final RegistryObject<DuckArmorItem> GOOSE_ARMOR = ITEMS.register("goose_armor",
            () -> new DuckArmorItem(DuckArmorItem.GOOSE_ARMOR_NBT, DuckArmorItem.GOOSE_ENTITY_ID,
                    new Item.Properties().stacksTo(1)));

    private static final RegistryObject<CreativeModeTab> TAB = TABS.register("duck_armor_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.duckarmor.duck_armor_tab"))
                    .icon(() -> new ItemStack(DUCK_ARMOR.get()))
                    .build());

    static {
        ModItems.DUCK_ARMOR = DUCK_ARMOR;
        ModItems.GOOSE_ARMOR = GOOSE_ARMOR;
    }

    public DuckarmorForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        EntityDataHelper.register(new ForgeEntityDataProvider());

        ITEMS.register(modBus);
        TABS.register(modBus);

        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onLivingHurt);
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onPlayerInteractEntity);

        modBus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if (event.getTabKey().location().equals(
                    new ResourceLocation(DuckarmorCommon.MODID, "duck_armor_tab"))) {
                event.accept(DUCK_ARMOR.get());
                event.accept(GOOSE_ARMOR.get());
            }
        });

        if (FMLEnvironment.dist == Dist.CLIENT) {
            // ClientTickEvent goes on the GAME bus, not the mod bus
            MinecraftForge.EVENT_BUS.addListener(ForgeClientEvents::onClientTick);
        }
    }
}