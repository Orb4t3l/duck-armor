package com.orbital.duckarmor.forge.client;

import com.orbital.duckarmor.client.renderer.layer.DuckArmorLayer;
import com.orbital.duckarmor.client.renderer.layer.GooseArmorLayer;
import com.orbital.duckarmor.item.DuckArmorItem;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ForgeClientEvents {

    public static void onAddEntityRenderLayers(EntityRenderersEvent.AddLayers event) {
        EntityType<?> duckType = ForgeRegistries.ENTITY_TYPES.getValue(DuckArmorItem.DUCK_ENTITY_ID);
        if (duckType != null) {
            EntityRenderer<?> r = event.getRenderer(duckType);
            if (r instanceof GeoEntityRenderer geo) {
                geo.addRenderLayer(new DuckArmorLayer<>(geo));
            }
        }

        EntityType<?> gooseType = ForgeRegistries.ENTITY_TYPES.getValue(DuckArmorItem.GOOSE_ENTITY_ID);
        if (gooseType != null) {
            EntityRenderer<?> r = event.getRenderer(gooseType);
            if (r instanceof GeoEntityRenderer geo) {
                geo.addRenderLayer(new GooseArmorLayer<>(geo));
            }
        }
    }
}
