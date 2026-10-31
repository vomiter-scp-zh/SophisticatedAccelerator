package com.vomiter.soph_accel.compat;

import com.vomiter.farmersbp.pot.CookingPotWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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
                BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("farmersbp", "cooking_pot_upgrade")),
                FarmerCompat::accelerateCooking
        );

        AccelerationRegistry.register(
                BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("farmersbp", "automatic_cooking_pot_upgrade")),
                FarmerCompat::accelerateCooking
        );
    }

    private static void accelerateCooking(IUpgradeWrapper wrapper,
                                          @Nullable Entity entity,
                                          Level level,
                                          BlockPos pos,
                                          int extraTickCount) {
        if (extraTickCount <= 0 || !wrapper.isEnabled() || level.isClientSide
                || !(wrapper instanceof CookingPotWrapper cookingUpgrade)
                || !(wrapper instanceof FarmerCookingPotAccessor accessor)) {
            return;
        }

        int cookTime = cookingUpgrade.getCookTime();
        accessor.soph_accel$setCookTime((int) Math.min(Integer.MAX_VALUE, (long) cookTime + extraTickCount));
    }
}
