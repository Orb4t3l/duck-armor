package com.orbital.duckarmor.platform;

import com.orbital.duckarmor.fabric.events.FabricEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.world.entity.LivingEntity;

public class ServicesImpl {

    public static void registerGameEvents() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof LivingEntity living) {
                return FabricEvents.onShearEntity(player, living);
            }
            return net.minecraft.world.InteractionResult.PASS;
        });
    }

    public static void registerClientEvents() {
    }
}
