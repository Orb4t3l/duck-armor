package com.orbital.duckarmor.neoforge.client;

import com.orbital.duckarmor.client.renderer.layer.DuckArmorLayer;
import com.orbital.duckarmor.client.renderer.layer.GooseArmorLayer;
import com.orbital.duckarmor.item.DuckArmorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@SuppressWarnings({"unchecked", "rawtypes"})
public class NeoForgeClientEvents {

    private static boolean duckLayerAdded = false;
    private static boolean gooseLayerAdded = false;

    // ClientTickEvent.Pre/Post replaced the old TickEvent.ClientTickEvent + Phase
    // enum entirely as of 1.20.6 — Post is the equivalent of the old Phase.END.
    public static void onClientTick(ClientTickEvent.Post event) {
        if (duckLayerAdded && gooseLayerAdded) return;

        Minecraft mc = Minecraft.getInstance();
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
