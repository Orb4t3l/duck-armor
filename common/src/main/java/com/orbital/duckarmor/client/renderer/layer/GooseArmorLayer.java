package com.orbital.duckarmor.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.orbital.duckarmor.client.model.GooseArmorGeoModel;
import com.orbital.duckarmor.item.DuckArmorItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GooseArmorLayer<T extends LivingEntity & GeoAnimatable> extends GeoRenderLayer<T> {

    private final GooseArmorGeoModel<T> armorModel = new GooseArmorGeoModel<>();

    public GooseArmorLayer(GeoEntityRenderer<T> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, T entity, BakedGeoModel bakedModel,
                       RenderType renderType, MultiBufferSource bufferSource,
                       VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        if (!DuckArmorItem.hasGooseArmor(entity)) return;

        BakedGeoModel armorBaked = armorModel.getBakedModel(armorModel.getModelResource(entity));
        if (armorBaked == null) return;

        bakedModel.topLevelBones().forEach(bone -> copyTransformsRecursive(bone, armorBaked));

        armorBaked.getBone("head").ifPresent(headBone -> {
            float headYaw = entity.getYHeadRot();
            float bodyYaw = entity.yBodyRot;
            float pitch = entity.getXRot();

            float yawDelta = Mth.wrapDegrees(headYaw - bodyYaw);

            headBone.setRotY(-yawDelta * Mth.DEG_TO_RAD);
            headBone.setRotX(-pitch * Mth.DEG_TO_RAD);
        });

        ResourceLocation texture = armorModel.getTextureResource(entity);
        RenderType armorRT = RenderType.entityCutoutNoCull(texture);

        getRenderer().reRender(armorBaked, poseStack, bufferSource, entity,
                armorRT, bufferSource.getBuffer(armorRT),
                partialTick, packedLight, packedOverlay, 255);
    }

    private static void copyTransformsRecursive(GeoBone from, BakedGeoModel target) {
        target.getBone(from.getName()).ifPresent(to -> {
            to.setRotX(from.getRotX());
            to.setRotY(from.getRotY());
            to.setRotZ(from.getRotZ());
            to.setPosX(from.getPosX());
            to.setPosY(from.getPosY());
            to.setPosZ(from.getPosZ());
            to.setScaleX(from.getScaleX());
            to.setScaleY(from.getScaleY());
            to.setScaleZ(from.getScaleZ());
        });
        from.getChildBones().forEach(child -> copyTransformsRecursive(child, target));
    }
}