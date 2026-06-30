package com.orbital.duckarmor.forge;

import com.orbital.duckarmor.DuckarmorCommon;
import com.orbital.duckarmor.forge.client.ForgeClientEvents;
import com.orbital.duckarmor.forge.events.ForgeEvents;
import com.orbital.duckarmor.forge.platform.ForgeEntityDataProvider;
import com.orbital.duckarmor.init.ModCreativeTabs;
import com.orbital.duckarmor.init.ModItems;
import com.orbital.duckarmor.platform.EntityDataHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(DuckarmorCommon.MODID)
public class DuckarmorForge {

    public DuckarmorForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        EntityDataHelper.register(new ForgeEntityDataProvider());
        DuckarmorCommon.init();

        // Game events go on the FORGE bus (entity/world events)
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onLivingHurt);
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onPlayerInteractEntity);

        // BuildCreativeModeTabContentsEvent fires on the MOD bus, not the FORGE bus
        modBus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if (event.getTab() == ModCreativeTabs.DUCK_ARMOR_TAB) {
                event.accept(new ItemStack(ModItems.DUCK_ARMOR.get()));
                event.accept(new ItemStack(ModItems.GOOSE_ARMOR.get()));
            }
        });

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modBus.addListener(ForgeClientEvents::onAddEntityRenderLayers);
        }
    }
}