package com.vomiter.soph_accel.data;

import com.vomiter.soph_accel.AcceleratorTier;
import com.vomiter.soph_accel.SophisticatedAccelerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class AcceleratorItemTagsProvider extends TagsProvider<Item> {
    private static final TagKey<Item> BACKPACK_UPGRADES = TagKey.create(
            Registries.ITEM, ResourceLocation.fromNamespaceAndPath("sophisticatedbackpacks", "upgrade"));
    private static final TagKey<Item> STORAGE_UPGRADES = TagKey.create(
            Registries.ITEM, ResourceLocation.fromNamespaceAndPath("sophisticatedstorage", "upgrade"));

    public AcceleratorItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup,
                                       ExistingFileHelper existingFileHelper) {
        super(output, Registries.ITEM, lookup, SophisticatedAccelerator.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        for (AcceleratorTier tier : AcceleratorTier.values()) {
            tag(BACKPACK_UPGRADES).add(ResourceKey.create(Registries.ITEM,
                    Objects.requireNonNull(SophisticatedAccelerator.ACCELERATORS.get(tier).getId())));
            tag(STORAGE_UPGRADES).add(ResourceKey.create(Registries.ITEM,
                    Objects.requireNonNull(SophisticatedAccelerator.ACCELERATORS_STORAGE.get(tier).getId())));
        }
    }
}
