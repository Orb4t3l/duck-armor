package com.orbital.duckarmor.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.orbital.duckarmor.client.model.DuckArmorGeoModel;
import com.orbital.duckarmor.item.DuckArmorItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class DuckArmorLayer<T extends LivingEntity & GeoAnimatable> extends GeoRenderLayer<T> {

    private final DuckArmorGeoModel<T> armorModel = new DuckArmorGeoModel<>();

    public DuckArmorLayer(GeoEntityRenderer<T> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, T entity, BakedGeoModel bakedModel,
                       RenderType renderType, MultiBufferSource bufferSource,
                       VertexConsumer buffer, float partialTick,
                       int packedLight, int packedOverlay) {

        if (!DuckArmorItem.hasDuckArmor(entity)) return;

        ResourceLocation texture      = armorModel.getTextureResource(entity);
        RenderType       armorRT      = RenderType.entityCutoutNoCull(texture);
        BakedGeoModel    armorBaked   = armorModel.getBakedModel(armorModel.getModelResource(entity));

        getRenderer().reRender(armorBaked, poseStack, bufferSource, entity,
                armorRT, bufferSource.getBuffer(armorRT),
                partialTick, packedLight, packedOverlay,
                1f, 1f, 1f, 1f);
    }
}
