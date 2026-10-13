package com.vomiter.soph_accel.compat;

import com.vomiter.soph_accel.AccelerationRegistry;
import com.vomiter.soph_accel.AcceleratorTier;
import com.vomiter.soph_accel.SophisticatedAccelerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;

import static com.vomiter.soph_accel.SophisticatedAccelerator.ACCELERATORS;

public class BackpackCompat {
    private BackpackCompat(){}

    static Item[] COOKING_UPGRADES = new Item[]{
            ModItems.SMELTING_UPGRADE.get(), ModItems.AUTO_SMELTING_UPGRADE.get(),
            ModItems.SMOKING_UPGRADE.get(), ModItems.AUTO_SMOKING_UPGRADE.get(),
            ModItems.BLASTING_UPGRADE.get(), ModItems.AUTO_BLASTING_UPGRADE.get()
    };

    public static void register(){
        for (Item cookingUpgrade : COOKING_UPGRADES) {
            AccelerationRegistry.register(cookingUpgrade, AccelerationRegistry::accelerateCookingDefault);
        }
    }

    public static void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ModItems.CREATIVE_TAB.getKey())) {
            for (AcceleratorTier tier : AcceleratorTier.values()) {
                event.accept(SophisticatedAccelerator.ACCELERATORS.get(tier));
            }
        }
    }

}
