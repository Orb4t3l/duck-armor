package com.orbital.duckarmor.neoforge.events;

import com.orbital.duckarmor.item.DuckArmorItem;
import com.orbital.duckarmor.neoforge.network.ModNetwork;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class NeoForgeEvents {

    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (id == null) return;

        if (id.equals(DuckArmorItem.DUCK_ENTITY_ID) && DuckArmorItem.hasDuckArmor(entity)) {
            event.setNewDamage(DuckArmorItem.applyArmorReduction(event.getNewDamage(), DuckArmorItem.DUCK_ARMOR_POINTS));
        } else if (id.equals(DuckArmorItem.GOOSE_ENTITY_ID) && DuckArmorItem.hasGooseArmor(entity)) {
            event.setNewDamage(DuckArmorItem.applyArmorReduction(event.getNewDamage(), DuckArmorItem.GOOSE_ARMOR_POINTS));
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

    public static void onEntityJoinLevel(net.neoforged.neoforge.event.entity.EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getEntity() instanceof LivingEntity living) {
            DuckArmorItem.reapplyIfNeeded(living);
        }
    }

    public static void onStartTracking(net.neoforged.neoforge.event.entity.player.PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof net.minecraft.server.level.ServerPlayer player
                && event.getTarget() instanceof LivingEntity living) {
            ModNetwork.queueSyncToPlayer(player, living);
        }
    }
}