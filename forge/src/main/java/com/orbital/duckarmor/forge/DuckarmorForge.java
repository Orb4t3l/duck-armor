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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DuckarmorCommon.MODID)
public class DuckarmorForge {

    private static final Logger LOGGER = LogManager.getLogger(DuckarmorCommon.MODID);

    public DuckarmorForge() {
        LOGGER.info("DuckArmor: starting Forge init");

        try {
            EntityDataHelper.register(new ForgeEntityDataProvider());
            LOGGER.info("DuckArmor: EntityDataHelper registered");
        } catch (Exception e) {
            LOGGER.error("DuckArmor: FAILED at EntityDataHelper.register", e);
            throw e;
        }

        try {
            ModItems.init();
            LOGGER.info("DuckArmor: ModItems.init() done");
        } catch (Exception e) {
            LOGGER.error("DuckArmor: FAILED at ModItems.init()", e);
            throw e;
        }

        try {
            ModCreativeTabs.init();
            LOGGER.info("DuckArmor: ModCreativeTabs.init() done");
        } catch (Exception e) {
            LOGGER.error("DuckArmor: FAILED at ModCreativeTabs.init()", e);
            throw e;
        }

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onLivingHurt);
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onPlayerInteractEntity);

        modBus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if (event.getTab() == ModCreativeTabs.DUCK_ARMOR_TAB) {
                event.accept(new ItemStack(ModItems.DUCK_ARMOR.get()));
                event.accept(new ItemStack(ModItems.GOOSE_ARMOR.get()));
            }
        });

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modBus.addListener(ForgeClientEvents::onAddEntityRenderLayers);
        }

        LOGGER.info("DuckArmor: Forge init complete");
    }
}