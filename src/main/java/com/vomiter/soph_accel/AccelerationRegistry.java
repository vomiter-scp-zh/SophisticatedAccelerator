package com.vomiter.soph_accel;

import com.vomiter.soph_accel.mixin.CookingLogicAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.CookingLogic;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.ICookingUpgrade;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Register during common setup; runtime dispatch uses only the frozen snapshot. */
public final class AccelerationRegistry {
    private static final Map<Item, AccelerationFunction> REGISTRATIONS = new LinkedHashMap<>();
    private static volatile Map<Item, AccelerationFunction> accelerators = Map.of();
    private static boolean frozen;

    private AccelerationRegistry() {}

    public static synchronized void register(Item upgrade, AccelerationFunction function) {
        if (frozen) {
            throw new IllegalStateException("Acceleration registrations are already frozen");
        }
        REGISTRATIONS.put(Objects.requireNonNull(upgrade), Objects.requireNonNull(function));
    }

    static synchronized void freeze() {
        if (!frozen) {
            accelerators = Map.copyOf(REGISTRATIONS);
            REGISTRATIONS.clear();
            frozen = true;
        }
    }

    /** Immutable runtime map. Empty until load complete. */
    public static Map<Item, AccelerationFunction> getAccelerators() {
        return accelerators;
    }

    public static void accelerateCookingDefault(IUpgradeWrapper wrapper,
                                          @Nullable Entity entity,
                                          Level level,
                                          BlockPos pos,
                                          int extraTickCount) {
        if (extraTickCount <= 0 || !wrapper.isEnabled() || level.isClientSide
                || !(wrapper instanceof ICookingUpgrade<?> cookingUpgrade)) {
            return;
        }
        CookingLogic<?> logic = cookingUpgrade.getCookingLogic();
        if (!logic.isCooking() || !logic.isBurning(level)) {
            return;
        }
        long now = level.getGameTime();
        long finishTime = logic.getCookTimeFinish();
        long burnOutTime = logic.getBurnTimeFinish();
        CookingLogicAccessor accessor = (CookingLogicAccessor) logic;

        boolean shouldUpdateTime = false;

        if (finishTime > now) {
            long acceleratedFinish = finishTime - Math.min(extraTickCount, finishTime - now);
            accessor.sophAccel$setCookTimeFinish(acceleratedFinish);
            shouldUpdateTime = true;
        }

        if (burnOutTime > now
                && wrapper.getUpgradeStack().getItem() instanceof AcceleratorUpgradeItem acceleratorUpgradeItem
                && AcceleratorConfig.accelerateFuel(acceleratorUpgradeItem.getTier())) {
            long acceleratedBurnFinish = burnOutTime - Math.min(extraTickCount, finishTime - now);
            accessor.sophAccel$setBurnTimeFinish(acceleratedBurnFinish);
            shouldUpdateTime = true;
        }

        if (shouldUpdateTime) accessor.sophAccel$updateTimes(level);
    }

    @FunctionalInterface
    public interface AccelerationFunction {
        void accelerate(IUpgradeWrapper wrapper, @Nullable Entity entity, Level level, BlockPos pos, int extraTickCount);
    }
}
