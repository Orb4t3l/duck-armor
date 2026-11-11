package com.orbital.duckarmor.fabric.mixin;

import com.orbital.duckarmor.fabric.events.FabricEvents;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(
            method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private float duckarmor$modifyDamage(float amount) {
        return FabricEvents.modifyDamage((LivingEntity) (Object) this, amount);
    }
}