package com.vomiter.soph_accel.data;

import com.vomiter.soph_accel.SophisticatedAccelerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = SophisticatedAccelerator.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class AcceleratorDataGenerators {
    private AcceleratorDataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        generator.addProvider(event.includeServer(), new AcceleratorRecipeProvider(output, event.getLookupProvider()));
        generator.addProvider(event.includeServer(), new AcceleratorItemTagsProvider(
                output, event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new AcceleratorItemModelProvider(
                output, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new AcceleratorLanguageProvider(output, "en_us"));
        generator.addProvider(event.includeClient(), new AcceleratorLanguageProvider(output, "zh_tw"));
    }
}
