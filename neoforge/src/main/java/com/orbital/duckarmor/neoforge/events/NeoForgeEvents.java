package com.orbital.duckarmor.neoforge.events;

import com.orbital.duckarmor.item.DuckArmorItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class NeoForgeEvents {

    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (id == null) return;

        if (id.equals(DuckArmorItem.DUCK_ENTITY_ID) && DuckArmorItem.hasDuckArmor(entity)) {
            event.setAmount(DuckArmorItem.applyArmorReduction(event.getAmount(), DuckArmorItem.DUCK_ARMOR_POINTS));
        } else if (id.equals(DuckArmorItem.GOOSE_ENTITY_ID) && DuckArmorItem.hasGooseArmor(entity)) {
            event.setAmount(DuckArmorItem.applyArmorReduction(event.getAmount(), DuckArmorItem.GOOSE_ARMOR_POINTS));
        }
    }

    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof LivingEntity target)) return;
        Player player = event.getEntity();
        ItemStack held = player.getItemInHand(event.getHand());
        if (!(held.getItem() instanceof ShearsItem)) return;

        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
        if (id == null) return;

        boolean isDuck = id.equals(DuckArmorItem.DUCK_ENTITY_ID) && DuckArmorItem.hasDuckArmor(target);
        boolean isGoose = id.equals(DuckArmorItem.GOOSE_ENTITY_ID) && DuckArmorItem.hasGooseArmor(target);

        if (isDuck || isGoose) {
            if (!target.level().isClientSide()) {
                String key = isDuck ? DuckArmorItem.DUCK_ARMOR_NBT : DuckArmorItem.GOOSE_ARMOR_NBT;
                DuckArmorItem.removeArmor(target, key, player);
            }
            event.setCanceled(true);
        }
    }
}
