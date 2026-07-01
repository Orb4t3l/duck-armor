package com.orbital.duckarmor.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.orbital.duckarmor.client.model.GooseArmorGeoModel;
import com.orbital.duckarmor.item.DuckArmorItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;

import java.util.Optional;

public class GooseArmorLayer<T extends LivingEntity & GeoAnimatable> extends GeoRenderLayer<T> {

    private static final Logger LOGGER = LogManager.getLogger("duckarmor");
    private final GooseArmorGeoModel<T> armorModel = new GooseArmorGeoModel<>();

    public GooseArmorLayer(GeoEntityRenderer<T> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, T entity, BakedGeoModel bakedModel,
                       RenderType renderType, MultiBufferSource bufferSource,
                       VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        if (!DuckArmorItem.hasGooseArmor(entity)) return;

        Optional<GeoBone> mainBoneOpt = bakedModel.getBone("main");
        if (mainBoneOpt.isEmpty()) {
            LOGGER.warn("DuckArmor: could not find 'main' bone in goose model");
            return;
        }

        GeoBone mainBone = mainBoneOpt.get();

        ResourceLocation texture = armorModel.getTextureResource(entity);
        RenderType armorRT = RenderType.entityCutoutNoCull(texture);

        BakedGeoModel armorBaked = armorModel.getBakedModel(armorModel.getModelResource(entity));
        if (armorBaked == null) {
            LOGGER.warn("DuckArmor: goose armor geo model not loaded");
            return;
        }

        poseStack.pushPose();

        RenderUtils.translateToPivotPoint(poseStack, mainBone);
        RenderUtils.rotateMatrixAroundBone(poseStack, mainBone);
        RenderUtils.scaleMatrixForBone(poseStack, mainBone);
        RenderUtils.translateAwayFromPivotPoint(poseStack, mainBone);

        getRenderer().reRender(armorBaked, poseStack, bufferSource, entity,
                armorRT, bufferSource.getBuffer(armorRT),
                partialTick, packedLight, packedOverlay,
                1f, 1f, 1f, 1f);

        poseStack.popPose();
    }
}
