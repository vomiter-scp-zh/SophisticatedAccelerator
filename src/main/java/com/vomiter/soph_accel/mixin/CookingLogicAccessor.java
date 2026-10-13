package com.vomiter.soph_accel.mixin;

import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.CookingLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = CookingLogic.class, remap = false)
public interface CookingLogicAccessor {

    @Invoker(value = "setBurnTimeFinish", remap = false)
    void sophAccel$setBurnTimeFinish(long finishTime);

    @Invoker(value = "setCookTimeFinish", remap = false)
    void sophAccel$setCookTimeFinish(long finishTime);

    @Invoker(value = "updateTimes", remap = false)
    void sophAccel$updateTimes(Level level);
}
