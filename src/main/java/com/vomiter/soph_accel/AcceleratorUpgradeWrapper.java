package com.vomiter.soph_accel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.ITickableUpgrade;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class AcceleratorUpgradeWrapper
        extends UpgradeWrapperBase<AcceleratorUpgradeWrapper, AcceleratorUpgradeItem> implements ITickableUpgrade {
    private long lastNormalTick = Long.MIN_VALUE;
    private int elapsedTicks;

    public AcceleratorUpgradeWrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> saveHandler) {
        super(storageWrapper, upgrade, saveHandler);
    }

    @Override
    public boolean hideSettingsTab() { return true; }

    @Override
    public void setEnabled(boolean enabled) {
        elapsedTicks = 0;
        lastNormalTick = Long.MIN_VALUE;
        super.setEnabled(enabled);
    }

    @Override
    public void tick(@Nullable Entity entity, Level level, BlockPos pos) {
        if (level.isClientSide() || !isEnabled() || AcceleratorTickHandler.isDraining()) {
            return;
        }
        long gameTime = level.getGameTime();
        // Inception or another caller may visit the same wrapper twice in one world tick.
        if (gameTime == lastNormalTick) {
            return;
        }
        lastNormalTick = gameTime;
        if (++elapsedTicks < AcceleratorConfig.interval(upgradeItem.getTier())) {
            return;
        }
        elapsedTicks = 0;
        int extraTicks = AcceleratorConfig.extraTickCount(upgradeItem.getTier());
        if (extraTicks > 0) {
            AcceleratorTickHandler.enqueue(this, entity, level, pos, extraTicks);
        }
    }

    void accelerate(@Nullable Entity entity, Level level, BlockPos pos, int extraTicks, ItemStack accelerator) {
        if (level.isClientSide() || (entity != null && (entity.isRemoved() || entity.level() != level))) {
            return;
        }
        Map<Item, AccelerationRegistry.AccelerationFunction> accelerators = AccelerationRegistry.getAccelerators();
        // Slot wrappers stay local; Inception's flattened interface list is not used.
        List<Map.Entry<Integer, IUpgradeWrapper>> targets = new ArrayList<>(
                storageWrapper.getUpgradeHandler().getSlotWrappers().entrySet());
        for (Map.Entry<Integer, IUpgradeWrapper> target : targets) {
            if (!isEnabled() || !isStillInstalled()) {
                return;
            }
            IUpgradeWrapper wrapper = target.getValue();
            if (storageWrapper.getUpgradeHandler().getSlotWrappers().get(target.getKey()) != wrapper
                    || !wrapper.isEnabled()) {
                continue;
            }
            AccelerationRegistry.AccelerationFunction function = accelerators.get(wrapper.getUpgradeStack().getItem());
            if (function != null) {
                function.accelerate(wrapper, entity, level, pos, extraTicks);
            }
        }
    }

    private boolean isStillInstalled() {
        return storageWrapper.getUpgradeHandler().getSlotWrappers().values().stream().anyMatch(wrapper -> wrapper == this);
    }

}
