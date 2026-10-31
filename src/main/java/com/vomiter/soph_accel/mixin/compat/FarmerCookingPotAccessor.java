package com.vomiter.soph_accel.mixin.compat;

import com.vomiter.farmersbp.pot.CookingPotWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = CookingPotWrapper.class, remap = false)
public interface FarmerCookingPotAccessor {
    @Accessor("cookTime")
    void soph_accel$setCookTime(int i);
}
