package com.orbital.duckarmor.item;

import com.orbital.duckarmor.init.ModItems;
import com.orbital.duckarmor.platform.EntityDataHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DuckArmorItem extends Item {

    private static final Logger LOGGER = LogManager.getLogger("duckarmor");

    public static final String DUCK_ARMOR_NBT = "duckarmor:duck_armor";
    public static final String GOOSE_ARMOR_NBT = "duckarmor:goose_armor";

    public static final int DUCK_ARMOR_POINTS = 8;
    public static final int GOOSE_ARMOR_POINTS = 11;

    public static final ResourceLocation DUCK_ENTITY_ID = new ResourceLocation("untitledduckmod", "duck");
    public static final ResourceLocation GOOSE_ENTITY_ID = new ResourceLocation("untitledduckmod", "goose");

    public static ArmorSyncCallback syncCallback;

    public interface ArmorSyncCallback {
        void sync(LivingEntity entity, String nbtKey, boolean applied);
    }

    private final String nbtKey;
    private final ResourceLocation targetEntityId;

    public DuckArmorItem(String nbtKey, ResourceLocation targetEntityId, Properties props) {
        super(props);
        this.nbtKey = nbtKey;
        this.targetEntityId = targetEntityId;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player,
                                                  LivingEntity target, InteractionHand hand) {
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
        LOGGER.info("DuckArmor: interactLivingEntity called, target id = {}, expected = {}, clientSide = {}",
                id, targetEntityId, target.level().isClientSide());

        if (!targetEntityId.equals(id)) {
            LOGGER.info("DuckArmor: entity id mismatch, passing");
            return InteractionResult.PASS;
        }
        if (target.isBaby()) {
            LOGGER.info("DuckArmor: target is baby, passing");
            return InteractionResult.PASS;
        }
        if (EntityDataHelper.getBoolean(target, nbtKey)) {
            LOGGER.info("DuckArmor: target already has armor, passing");
            return InteractionResult.PASS;
        }

        if (!target.level().isClientSide()) {
            LOGGER.info("DuckArmor: writing NBT flag on server side");
            EntityDataHelper.putBoolean(target, nbtKey, true);
            LOGGER.info("DuckArmor: readback check immediately after write = {}",
                    EntityDataHelper.getBoolean(target, nbtKey));
            target.playSound(SoundEvents.ARMOR_EQUIP_IRON, 1.0f, 1.0f);
            if (!player.isCreative()) stack.shrink(1);
            if (syncCallback != null) syncCallback.sync(target, nbtKey, true);
        }
        return InteractionResult.sidedSuccess(target.level().isClientSide());
    }

    public static boolean hasDuckArmor(LivingEntity entity) {
        return EntityDataHelper.getBoolean(entity, DUCK_ARMOR_NBT);
    }

    public static boolean hasGooseArmor(LivingEntity entity) {
        return EntityDataHelper.getBoolean(entity, GOOSE_ARMOR_NBT);
    }

    public static void removeArmor(LivingEntity entity, String nbtKey, Player shearer) {
        EntityDataHelper.remove(entity, nbtKey);
        entity.playSound(SoundEvents.ARMOR_EQUIP_IRON, 1.0f, 0.5f);
        if (!entity.level().isClientSide()) {
            Item drop = nbtKey.equals(DUCK_ARMOR_NBT) ? ModItems.DUCK_ARMOR.get() : ModItems.GOOSE_ARMOR.get();
            entity.spawnAtLocation(new ItemStack(drop));
            if (syncCallback != null) syncCallback.sync(entity, nbtKey, false);
        }
    }

    public static float applyArmorReduction(float raw, int armorPoints) {
        float epf = 2f;
        float effective = Mth.clamp((float) armorPoints - raw / epf, (float) armorPoints * 0.2f, 20f);
        return raw * (1f - effective / 25f);
    }
}