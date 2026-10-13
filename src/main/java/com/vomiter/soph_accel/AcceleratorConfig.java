package com.vomiter.soph_accel;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.EnumMap;
import java.util.Map;

public final class AcceleratorConfig {
    public static final ForgeConfigSpec SPEC;
    private static final Map<AcceleratorTier, TierSettings> TIERS = new EnumMap<>(AcceleratorTier.class);

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("tiers");
        for (AcceleratorTier tier : AcceleratorTier.values()) {
            builder.push(tier.id());
            ForgeConfigSpec.IntValue interval = builder
                    .comment("Number of normal server ticks of this backpack between acceleration bursts.")
                    .defineInRange("interval", tier.defaultInterval(), 1, Integer.MAX_VALUE);
            ForgeConfigSpec.IntValue count = builder
                    .comment("Acceleration amount passed to each registered function per burst. Cooking shortens its wait by this many ticks. Zero disables acceleration.")
                    .defineInRange("extra_tick_count", tier.defaultExtraTickCount(), 0, 1024);
            ForgeConfigSpec.BooleanValue accelerateFuel = builder
                    .comment("If true, this upgrade will also accelerate the burning time ticking, making it burn out earlier.")
                    .define("accelerate_fuel_buring", false);

            TIERS.put(tier, new TierSettings(interval, count, accelerateFuel));
            builder.pop();
        }
        builder.pop();
        SPEC = builder.build();
    }

    private AcceleratorConfig() {}

    public static int interval(AcceleratorTier tier) { return TIERS.get(tier).interval().get(); }
    public static int extraTickCount(AcceleratorTier tier) { return TIERS.get(tier).extraTickCount().get(); }
    public static boolean accelerateFuel(AcceleratorTier tier){return TIERS.get(tier).accelerateFuelConsume().get();}

    private record TierSettings(ForgeConfigSpec.IntValue interval, ForgeConfigSpec.IntValue extraTickCount, ForgeConfigSpec.BooleanValue accelerateFuelConsume) {}
}
