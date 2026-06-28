package com.orbital.duckarmor.client.model;

import com.orbital.duckarmor.DuckarmorCommon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class GooseArmorGeoModel<T extends LivingEntity & GeoAnimatable> extends GeoModel<T> {

    private static final ResourceLocation MODEL =
            new ResourceLocation(DuckarmorCommon.MODID, "geo/goose_armor.geo.json");
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DuckarmorCommon.MODID, "textures/entity/goose_armor.png");
    private static final ResourceLocation ANIMATIONS =
            new ResourceLocation(DuckarmorCommon.MODID, "animations/goose_armor.animation.json");

    @Override public ResourceLocation getModelResource(T a)     { return MODEL;      }
    @Override public ResourceLocation getTextureResource(T a)   { return TEXTURE;    }
    @Override public ResourceLocation getAnimationResource(T a) { return ANIMATIONS; }
}
