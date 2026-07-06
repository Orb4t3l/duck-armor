package com.orbital.duckarmor.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.orbital.duckarmor.client.model.DuckArmorGeoModel;
import com.orbital.duckarmor.item.DuckArmorItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
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
                       VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        if (!DuckArmorItem.hasDuckArmor(entity)) return;

        BakedGeoModel armorBaked = armorModel.getBakedModel(armorModel.getModelResource(entity));
        if (armorBaked == null) return;

        bakedModel.topLevelBones().forEach(bone -> copyTransformsRecursive(bone, armorBaked));

        armorBaked.getBone("head").ifPresent(headBone -> {
            // Use interpolated view rotation, not the raw instant fields, so the
            // head doesn't visibly snap/jitter between tick updates at high framerate.
            float headYaw = entity.getViewYRot(partialTick);
            float bodyYaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
            float pitch = entity.getViewXRot(partialTick);

            float yawDelta = Mth.wrapDegrees(headYaw - bodyYaw);

            headBone.setRotY(-yawDelta * Mth.DEG_TO_RAD);
            // Pitch was inverted: looking up (negative pitch) should rotate the
            // bone the opposite way from looking down, hence the extra negation.
            headBone.setRotX(-pitch * Mth.DEG_TO_RAD);
        });

        ResourceLocation texture = armorModel.getTextureResource(entity);
        RenderType armorRT = RenderType.entityCutoutNoCull(texture);

        getRenderer().reRender(armorBaked, poseStack, bufferSource, entity,
                armorRT, bufferSource.getBuffer(armorRT),
                partialTick, packedLight, packedOverlay,
                1f, 1f, 1f, 1f);
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