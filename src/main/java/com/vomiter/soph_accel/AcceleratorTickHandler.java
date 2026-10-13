package com.vomiter.soph_accel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public final class AcceleratorTickHandler {

    static void init(){
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(AcceleratorTickHandler::onServerStopped);
        bus.addListener(AcceleratorTickHandler::onServerTick);
    }

    private static final ThreadLocal<QueueState> QUEUE = ThreadLocal.withInitial(QueueState::new);

    private AcceleratorTickHandler() {}

    static boolean isDraining() { return QUEUE.get().draining; }

    static void enqueue(AcceleratorUpgradeWrapper wrapper, @Nullable Entity entity, Level level, BlockPos pos, int count) {
        QueueState state = QUEUE.get();
        if (!state.draining) {
            state.pending.putIfAbsent(wrapper, new PendingTick(wrapper, entity, level, pos.immutable(), count, wrapper.getUpgradeStack()));
        }
    }

    public static void onServerTick(TickEvent.ServerTickEvent event) {
        QueueState state = QUEUE.get();
        if (event.phase == TickEvent.Phase.START) {
            state.pending.clear();
            return;
        }
        ArrayList<PendingTick> work = new ArrayList<>(state.pending.values());
        state.pending.clear();
        state.draining = true;
        try {
            for (PendingTick pending : work) {
                pending.wrapper().accelerate(pending.entity(), pending.level(), pending.pos(), pending.count(), pending.accelerator());
            }
        } finally {
            state.draining = false;
            state.pending.clear();
        }
    }

    public static void onServerStopped(ServerStoppedEvent event) { QUEUE.remove(); }

    private static final class QueueState {
        final Map<AcceleratorUpgradeWrapper, PendingTick> pending = new LinkedHashMap<>();
        boolean draining;
    }

    private record PendingTick(AcceleratorUpgradeWrapper wrapper, @Nullable Entity entity, Level level, BlockPos pos, int count, ItemStack accelerator) {}
}
