package com.vomiter.soph_accel.data;

import com.vomiter.soph_accel.AcceleratorTier;
import com.vomiter.soph_accel.SophisticatedAccelerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public final class AcceleratorItemModelProvider extends ItemModelProvider {
    public AcceleratorItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SophisticatedAccelerator.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (AcceleratorTier tier : AcceleratorTier.values()) {
            basicItem(SophisticatedAccelerator.ACCELERATORS.get(tier).get());
            basicItem(SophisticatedAccelerator.ACCELERATORS_STORAGE.get(tier).get());
        }
    }
}
