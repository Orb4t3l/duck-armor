package com.orbital.duckarmor.fabric.client;

import com.orbital.duckarmor.DuckarmorCommon;
import com.orbital.duckarmor.client.renderer.layer.DuckArmorLayer;
import com.orbital.duckarmor.client.renderer.layer.GooseArmorLayer;
import com.orbital.duckarmor.item.DuckArmorItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DuckarmorFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        DuckarmorCommon.clientInit();
        registerArmorLayers();
    }

    private static void registerArmorLayers() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, renderer, registrationHelper, context) -> {
                    EntityType<?> duck = BuiltInRegistries.ENTITY_TYPE.get(DuckArmorItem.DUCK_ENTITY_ID);
                    EntityType<?> goose = BuiltInRegistries.ENTITY_TYPE.get(DuckArmorItem.GOOSE_ENTITY_ID);

                    if (entityType == duck && renderer instanceof GeoEntityRenderer geo) {
                        registrationHelper.register(new DuckArmorLayer<>(geo));
                    }
                    if (entityType == goose && renderer instanceof GeoEntityRenderer geo) {
                        registrationHelper.register(new GooseArmorLayer<>(geo));
                    }
                });
    }
}
