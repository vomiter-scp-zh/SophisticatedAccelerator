package com.vomiter.soph_accel;

import com.vomiter.soph_accel.compat.BackpackCompat;
import com.vomiter.soph_accel.compat.FarmerCompat;
import com.vomiter.soph_accel.compat.StorageCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeGroup;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

@Mod(SophisticatedAccelerator.MOD_ID)
public final class SophisticatedAccelerator {
    public static final String MOD_ID = "soph_accel";
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final Map<AcceleratorTier, RegistryObject<AcceleratorUpgradeItem>> ACCELERATORS = new EnumMap<>(AcceleratorTier.class);
    public static final Map<AcceleratorTier, RegistryObject<AcceleratorUpgradeItem>> ACCELERATORS_STORAGE = new EnumMap<>(AcceleratorTier.class);

    private static final IUpgradeCountLimitConfig LIMITS = new IUpgradeCountLimitConfig() {
        @Override
        public int getMaxUpgradesPerStorage(@NotNull String storageType, ResourceLocation upgradeName) {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getMaxUpgradesInGroupPerStorage(@NotNull String storageType, @NotNull UpgradeGroup group) {
            return Integer.MAX_VALUE;
        }
    };

    static {
        for (AcceleratorTier tier : AcceleratorTier.values()) {
            ACCELERATORS.put(tier, ITEMS.register(tier.id() + "_accelerator_upgrade",
                    () -> new AcceleratorUpgradeItem(LIMITS, tier)));
            ACCELERATORS_STORAGE.put(tier, ITEMS.register(tier.id() + "_storage_accelerator_upgrade",
                    () -> new AcceleratorUpgradeItem(LIMITS, tier)));

        }
    }

    public SophisticatedAccelerator() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        if (ModList.get().isLoaded("sophisticatedbackpacks")){
            bus.addListener(BackpackCompat::addCreativeTabContents);
        }
        if (ModList.get().isLoaded("sophisticatedstorage")){
            bus.addListener(StorageCompat::addCreativeTabContents);
        }

        bus.addListener(this::commonSetup);
        bus.addListener(this::loadComplete);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AcceleratorConfig.SPEC);
        AcceleratorTickHandler.init();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("farmersbp")){
            event.enqueueWork(FarmerCompat::register);
        }
        if (ModList.get().isLoaded("sophisticatedbackpacks")){
            event.enqueueWork(BackpackCompat::register);
        }
        if (ModList.get().isLoaded("sophisticatedstorage")){
            event.enqueueWork(StorageCompat::register);
        }
    }

    private void loadComplete(FMLLoadCompleteEvent event) {
        // All mods' common setup listeners and enqueued work have completed by this stage.
        event.enqueueWork(AccelerationRegistry::freeze);
    }
}
