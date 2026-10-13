package com.vomiter.soph_accel.compat;

import com.vomiter.farmersbp.FarmerSBP;
import com.vomiter.farmersbp.pot.CookingPotWrapper;
import com.vomiter.soph_accel.AccelerationRegistry;
import com.vomiter.soph_accel.mixin.compat.FarmerCookingPotAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeWrapper;

import javax.annotation.Nullable;

public class FarmerCompat {
    public static synchronized void register(){
        AccelerationRegistry.register(
                FarmerSBP.COOKING_POT.get(),
                FarmerCompat::accelerateCooking
        );

        AccelerationRegistry.register(
                FarmerSBP.AUTOMATIC_COOKING_POT.get(),
                FarmerCompat::accelerateCooking
        );
    }

    private static void accelerateCooking(IUpgradeWrapper wrapper,
                                          @Nullable Entity entity,
                                          Level level,
                                          BlockPos pos,
                                          int extraTickCount) {
        if (extraTickCount <= 0 || !wrapper.isEnabled() || level.isClientSide
                || !(wrapper instanceof CookingPotWrapper cookingUpgrade)) {
            return;
        }

        if ((Object)cookingUpgrade instanceof FarmerCookingPotAccessor accessor){
            int cookTime = cookingUpgrade.getCookTime();
            cookTime += extraTickCount;
            accessor.soph_accel$setCookTime(cookTime);
        }
    }
}
