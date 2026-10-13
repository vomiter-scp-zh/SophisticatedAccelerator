package com.vomiter.soph_accel;

public enum AcceleratorTier {
    BASIC("basic", 20, 1),
    IRON("iron", 5, 1),
    GOLD("gold", 5, 2),
    DIAMOND("diamond", 3, 4);

    private final String id;
    private final int defaultInterval;
    private final int defaultExtraTickCount;

    AcceleratorTier(String id, int defaultInterval, int defaultExtraTickCount) {
        this.id = id;
        this.defaultInterval = defaultInterval;
        this.defaultExtraTickCount = defaultExtraTickCount;
    }

    public String id() { return id; }
    public int defaultInterval() { return defaultInterval; }
    public int defaultExtraTickCount() { return defaultExtraTickCount; }
}
