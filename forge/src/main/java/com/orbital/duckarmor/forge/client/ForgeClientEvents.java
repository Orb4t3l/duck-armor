package com.orbital.duckarmor.forge.client;

import com.orbital.duckarmor.client.renderer.layer.DuckArmorLayer;
import com.orbital.duckarmor.client.renderer.layer.GooseArmorLayer;
import com.orbital.duckarmor.item.DuckArmorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ForgeClientEvents {

    private static final Logger LOGGER = LogManager.getLogger("duckarmor");
    private static boolean duckLayerAdded = false;
    private static boolean gooseLayerAdded = false;

    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (duckLayerAdded && gooseLayerAdded) return;

        var mc = Minecraft.getInstance();
        if (mc.level == null) return;

        for (var entity : mc.level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity living)) continue;

            ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(living.getType());

            if (!duckLayerAdded && DuckArmorItem.DUCK_ENTITY_ID.equals(id)) {
                var renderer = mc.getEntityRenderDispatcher().getRenderer(living);
                if (renderer instanceof GeoEntityRenderer geo) {
                    geo.addRenderLayer(new DuckArmorLayer<>(geo));
                    duckLayerAdded = true;
                    LOGGER.info("DuckArmor: duck armor layer added (lazy)");
                } else {
                    LOGGER.error("DuckArmor: duck renderer is {} — not a GeoEntityRenderer",
                            renderer == null ? "null" : renderer.getClass().getName());
                }
            }

            if (!gooseLayerAdded && DuckArmorItem.GOOSE_ENTITY_ID.equals(id)) {
                var renderer = mc.getEntityRenderDispatcher().getRenderer(living);
                if (renderer instanceof GeoEntityRenderer geo) {
                    geo.addRenderLayer(new GooseArmorLayer<>(geo));
                    gooseLayerAdded = true;
                    LOGGER.info("DuckArmor: goose armor layer added (lazy)");
                } else {
                    LOGGER.error("DuckArmor: goose renderer is {} — not a GeoEntityRenderer",
                            renderer == null ? "null" : renderer.getClass().getName());
                }
            }

            if (duckLayerAdded && gooseLayerAdded) break;
        }
    }
}