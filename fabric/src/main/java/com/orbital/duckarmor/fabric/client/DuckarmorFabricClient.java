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
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * Fabric API's LivingEntityFeatureRendererRegistrationCallback only accepts
 * vanilla LivingEntityRenderer<T, ? extends EntityModel<T>> — GeckoLib's
 * GeoEntityRenderer doesn't fit that hierarchy at all, so that callback can
 * never work here. Instead we mirror the Forge approach: wait for a real
 * duck/goose to exist in the world, grab its actual renderer instance via
 * the entity render dispatcher, and add our layer directly.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class DuckarmorFabricClient implements ClientModInitializer {

    private static boolean duckLayerAdded = false;
    private static boolean gooseLayerAdded = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(Minecraft mc) {
        if (duckLayerAdded && gooseLayerAdded) return;
        if (mc.level == null) return;

        for (var entity : mc.level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity living)) continue;

            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(living.getType());

            if (!duckLayerAdded && DuckArmorItem.DUCK_ENTITY_ID.equals(id)) {
                var renderer = mc.getEntityRenderDispatcher().getRenderer(living);
                if (renderer instanceof GeoEntityRenderer geo) {
                    geo.addRenderLayer(new DuckArmorLayer<>(geo));
                    duckLayerAdded = true;
                }
            }

            if (!gooseLayerAdded && DuckArmorItem.GOOSE_ENTITY_ID.equals(id)) {
                var renderer = mc.getEntityRenderDispatcher().getRenderer(living);
                if (renderer instanceof GeoEntityRenderer geo) {
                    geo.addRenderLayer(new GooseArmorLayer<>(geo));
                    gooseLayerAdded = true;
                }
            }

            if (duckLayerAdded && gooseLayerAdded) break;
        }
    }
}