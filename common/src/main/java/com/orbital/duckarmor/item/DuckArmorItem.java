package com.orbital.duckarmor.item;

import com.orbital.duckarmor.init.ModItems;
import com.orbital.duckarmor.platform.ArmorPersistenceCache;
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

public class DuckArmorItem extends Item {

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
        if (!targetEntityId.equals(id)) return InteractionResult.PASS;
        if (target.isBaby()) return InteractionResult.PASS;
        if (EntityDataHelper.getBoolean(target, nbtKey)) return InteractionResult.PASS;

        if (!target.level().isClientSide()) {
            EntityDataHelper.putBoolean(target, nbtKey, true);
            ArmorPersistenceCache.register(target.getUUID(), nbtKey);
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
        ArmorPersistenceCache.unregister(entity.getUUID(), nbtKey);
        entity.playSound(SoundEvents.ARMOR_EQUIP_IRON, 1.0f, 0.5f);
        if (!entity.level().isClientSide()) {
            Item drop = nbtKey.equals(DUCK_ARMOR_NBT) ? ModItems.DUCK_ARMOR.get() : ModItems.GOOSE_ARMOR.get();
            entity.spawnAtLocation(new ItemStack(drop));
            if (syncCallback != null) syncCallback.sync(entity, nbtKey, false);
        }
    }

    /**
     * Called from each platform's "entity joined level" hook. If this entity's
     * UUID is in the persistence cache but its live persistentData/mixin flag
     * doesn't have it (which happens after dimension-travel reconstruction),
     * reapply the flag and re-sync it to clients.
     */
    public static void reapplyIfNeeded(LivingEntity entity) {
        for (String key : ArmorPersistenceCache.getAll(entity.getUUID())) {
            if (!EntityDataHelper.getBoolean(entity, key)) {
                EntityDataHelper.putBoolean(entity, key, true);
                if (syncCallback != null) syncCallback.sync(entity, key, true);
            }
        }
    }

    public static float applyArmorReduction(float raw, int armorPoints) {
        float epf = 2f;
        float effective = Mth.clamp((float) armorPoints - raw / epf, (float) armorPoints * 0.2f, 20f);
        return raw * (1f - effective / 25f);
    }
}