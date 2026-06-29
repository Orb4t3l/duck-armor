package com.orbital.duckarmor.fabric.mixin;

import com.orbital.duckarmor.fabric.data.DuckarmorEntityData;
import com.orbital.duckarmor.fabric.events.FabricEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements DuckarmorEntityData {

    @Unique
    private CompoundTag duckarmor$data = new CompoundTag();

    @Override
    public CompoundTag duckarmor$getData() {
        return this.duckarmor$data;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void duckarmor$writeNbt(CompoundTag tag, CallbackInfo ci) {
        tag.put("DuckarmorData", this.duckarmor$data);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void duckarmor$readNbt(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("DuckarmorData")) {
            this.duckarmor$data = tag.getCompound("DuckarmorData");
        }
    }

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
