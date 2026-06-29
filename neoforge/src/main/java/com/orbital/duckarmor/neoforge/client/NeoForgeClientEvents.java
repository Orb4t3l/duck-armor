package com.orbital.duckarmor.neoforge.client;

import com.orbital.duckarmor.client.renderer.layer.DuckArmorLayer;
import com.orbital.duckarmor.client.renderer.layer.GooseArmorLayer;
import com.orbital.duckarmor.item.DuckArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@SuppressWarnings({"unchecked", "rawtypes"})
public class NeoForgeClientEvents {

    public static void onAddEntityRenderLayers(EntityRenderersEvent.AddLayers event) {
        addLayer(event, DuckArmorItem.DUCK_ENTITY_ID, true);
        addLayer(event, DuckArmorItem.GOOSE_ENTITY_ID, false);
    }

    private static <T extends LivingEntity> void addLayer(
            EntityRenderersEvent.AddLayers event, ResourceLocation id, boolean isDuck) {
        EntityType<T> type = (EntityType<T>) ForgeRegistries.ENTITY_TYPES.getValue(id);
        if (type == null) return;
        Object renderer = event.getRenderer(type);
        if (!(renderer instanceof GeoEntityRenderer geo)) return;
        if (isDuck) {
            geo.addRenderLayer(new DuckArmorLayer<>(geo));
        } else {
            geo.addRenderLayer(new GooseArmorLayer<>(geo));
        }
    }
}