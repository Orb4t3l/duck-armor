package com.orbital.duckarmor.fabric.mixin;

import com.orbital.duckarmor.fabric.data.DuckarmorEntityData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements DuckarmorEntityData {

    @Unique
    private static final Logger duckarmor$logger = LogManager.getLogger("duckarmor");

    @Unique
    private CompoundTag duckarmor$data = new CompoundTag();

    @Override
    public CompoundTag duckarmor$getData() {
        return this.duckarmor$data;
    }

    @Inject(method = "saveWithoutId", at = @At("TAIL"))
    private void duckarmor$write(CompoundTag tag, CallbackInfoReturnable<Boolean> cir) {
        tag.put("DuckarmorData", this.duckarmor$data);
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void duckarmor$read(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("DuckarmorData")) {
            this.duckarmor$data = tag.getCompound("DuckarmorData");
            duckarmor$logger.info("DuckArmor: loaded DuckarmorData = {}", this.duckarmor$data);
        }
    }
}