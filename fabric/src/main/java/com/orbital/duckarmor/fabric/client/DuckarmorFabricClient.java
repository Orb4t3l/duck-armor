package com.orbital.duckarmor.fabric.client;

import com.orbital.duckarmor.client.renderer.layer.DuckArmorLayer;
import com.orbital.duckarmor.client.renderer.layer.GooseArmorLayer;
import com.orbital.duckarmor.item.DuckArmorItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DuckarmorFabricClient implements ClientModInitializer {

    private static final Logger LOGGER = LogManager.getLogger("duckarmor");
    private static boolean duckLayerAdded = false;
    private static boolean gooseLayerAdded = false;

    @Override
    public void onInitializeClient() {
        LOGGER.info("DuckArmor: onInitializeClient fired, registering tick listener");
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(Minecraft mc) {
        if (duckLayerAdded && gooseLayerAdded) return;
        if (mc.level == null) return;

        for (var entity : mc.level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity living)) continue;

            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(living.getType());

            if (!duckLayerAdded && DuckArmorItem.DUCK_ENTITY_ID.equals(id)) {
                LOGGER.info("DuckArmor: found duck entity, checking renderer");
                var renderer = mc.getEntityRenderDispatcher().getRenderer(living);
                LOGGER.info("DuckArmor: duck renderer class = {}", renderer.getClass().getName());
                if (renderer instanceof GeoEntityRenderer geo) {
                    geo.addRenderLayer(new DuckArmorLayer<>(geo));
                    duckLayerAdded = true;
                    LOGGER.info("DuckArmor: duck armor layer added successfully");
                } else {
                    LOGGER.error("DuckArmor: duck renderer is NOT a GeoEntityRenderer");
                }
            }

            if (!gooseLayerAdded && DuckArmorItem.GOOSE_ENTITY_ID.equals(id)) {
                LOGGER.info("DuckArmor: found goose entity, checking renderer");
                var renderer = mc.getEntityRenderDispatcher().getRenderer(living);
                LOGGER.info("DuckArmor: goose renderer class = {}", renderer.getClass().getName());
                if (renderer instanceof GeoEntityRenderer geo) {
                    geo.addRenderLayer(new GooseArmorLayer<>(geo));
                    gooseLayerAdded = true;
                    LOGGER.info("DuckArmor: goose armor layer added successfully");
                } else {
                    LOGGER.error("DuckArmor: goose renderer is NOT a GeoEntityRenderer");
                }
            }

            if (duckLayerAdded && gooseLayerAdded) break;
        }
    }
}