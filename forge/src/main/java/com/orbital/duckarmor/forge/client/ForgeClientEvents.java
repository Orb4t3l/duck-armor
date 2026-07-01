package com.orbital.duckarmor.forge.client;

import com.orbital.duckarmor.client.renderer.layer.DuckArmorLayer;
import com.orbital.duckarmor.client.renderer.layer.GooseArmorLayer;
import com.orbital.duckarmor.item.DuckArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ForgeClientEvents {

    private static final Logger LOGGER = LogManager.getLogger("assets/duckarmor");

    public static void onAddEntityRenderLayers(EntityRenderersEvent.AddLayers event) {
        LOGGER.info("DuckArmor: onAddEntityRenderLayers fired");
        addLayer(event, DuckArmorItem.DUCK_ENTITY_ID, true);
        addLayer(event, DuckArmorItem.GOOSE_ENTITY_ID, false);
    }

    private static <T extends LivingEntity> void addLayer(
            EntityRenderersEvent.AddLayers event, ResourceLocation id, boolean isDuck) {

        LOGGER.info("DuckArmor: looking up entity type '{}'", id);

        EntityType<T> type = (EntityType<T>) ForgeRegistries.ENTITY_TYPES.getValue(id);
        if (type == null) {
            LOGGER.error("DuckArmor: entity type '{}' NOT FOUND in registry — check DUCK_ENTITY_ID/GOOSE_ENTITY_ID", id);
            // Print all registered entity types that contain "duck" or "goose" to help find the real ID
            ForgeRegistries.ENTITY_TYPES.getKeys().stream()
                    .filter(k -> k.toString().contains("duck") || k.toString().contains("goose")
                            || k.toString().contains("untitled"))
                    .forEach(k -> LOGGER.info("DuckArmor: candidate entity type: {}", k));
            return;
        }
        LOGGER.info("DuckArmor: found entity type '{}'", id);

        Object renderer = event.getRenderer(type);
        if (renderer == null) {
            LOGGER.error("DuckArmor: getRenderer returned null for '{}' — renderer not registered yet?", id);
            return;
        }
        LOGGER.info("DuckArmor: renderer for '{}' is {}", id, renderer.getClass().getName());

        if (!(renderer instanceof GeoEntityRenderer geo)) {
            LOGGER.error("DuckArmor: renderer for '{}' is NOT a GeoEntityRenderer — it's a {}. " +
                    "The Untitled Duck Mod must use GeckoLib for this to work.", id, renderer.getClass().getName());
            return;
        }

        LOGGER.info("DuckArmor: adding {} armor layer to '{}'", isDuck ? "duck" : "goose", id);
        if (isDuck) {
            geo.addRenderLayer(new DuckArmorLayer<>(geo));
        } else {
            geo.addRenderLayer(new GooseArmorLayer<>(geo));
        }
        LOGGER.info("DuckArmor: layer added successfully for '{}'", id);
    }
}