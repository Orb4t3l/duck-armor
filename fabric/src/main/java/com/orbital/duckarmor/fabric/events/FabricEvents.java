package com.orbital.duckarmor.fabric.events;

import com.orbital.duckarmor.item.DuckArmorItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;

public class FabricEvents {

    public static float modifyDamage(LivingEntity entity, float amount) {
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (id == null) return amount;

        if (id.equals(DuckArmorItem.DUCK_ENTITY_ID) && DuckArmorItem.hasDuckArmor(entity)) {
            return DuckArmorItem.applyArmorReduction(amount, DuckArmorItem.DUCK_ARMOR_POINTS);
        }
        if (id.equals(DuckArmorItem.GOOSE_ENTITY_ID) && DuckArmorItem.hasGooseArmor(entity)) {
            return DuckArmorItem.applyArmorReduction(amount, DuckArmorItem.GOOSE_ARMOR_POINTS);
        }
        return amount;
    }

    public static InteractionResult onShearEntity(Player player, LivingEntity target) {
        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof ShearsItem)) return InteractionResult.PASS;

        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
        if (id == null) return InteractionResult.PASS;

        boolean isDuck = id.equals(DuckArmorItem.DUCK_ENTITY_ID) && DuckArmorItem.hasDuckArmor(target);
        boolean isGoose = id.equals(DuckArmorItem.GOOSE_ENTITY_ID) && DuckArmorItem.hasGooseArmor(target);

        if (isDuck || isGoose) {
            if (!target.level().isClientSide()) {
                String key = isDuck ? DuckArmorItem.DUCK_ARMOR_NBT : DuckArmorItem.GOOSE_ARMOR_NBT;
                DuckArmorItem.removeArmor(target, key, player);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
