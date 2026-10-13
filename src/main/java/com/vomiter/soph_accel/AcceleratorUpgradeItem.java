package com.vomiter.soph_accel;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public final class AcceleratorUpgradeItem extends UpgradeItemBase<AcceleratorUpgradeWrapper> {
    private static final UpgradeType<AcceleratorUpgradeWrapper> TYPE = new UpgradeType<>(AcceleratorUpgradeWrapper::new);
    private final AcceleratorTier tier;

    public AcceleratorUpgradeItem(IUpgradeCountLimitConfig limits, AcceleratorTier tier) {
        super(limits);
        this.tier = tier;
    }

    public AcceleratorTier getTier() { return tier; }

    @Override
    public @NotNull UpgradeType<AcceleratorUpgradeWrapper> getType() { return TYPE; }

    @Override
    public @NotNull List<UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of(new UpgradeConflictDefinition(AcceleratorUpgradeItem.class::isInstance, 0,
                Component.translatable("error.soph_accel.accelerator_exists")));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("item.soph_accel.accelerator_upgrade.tooltip").withStyle(ChatFormatting.DARK_GRAY));
        // SERVER config is world-specific and may not be loaded in the creative menu/title screen.
        int interval = AcceleratorConfig.SPEC.isLoaded() ? AcceleratorConfig.interval(tier) : tier.defaultInterval();
        int count = AcceleratorConfig.SPEC.isLoaded() ? AcceleratorConfig.extraTickCount(tier) : tier.defaultExtraTickCount();
        tooltip.add(Component.translatable("item.soph_accel.accelerator_upgrade.stats", interval, count)
                .withStyle(ChatFormatting.GRAY));
    }
}
