package com.vomiter.soph_accel.data;

import com.vomiter.soph_accel.AcceleratorTier;
import com.vomiter.soph_accel.AcceleratorUpgradeItem;
import com.vomiter.soph_accel.SophisticatedAccelerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public final class AcceleratorRecipeProvider extends RecipeProvider {
    public AcceleratorRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        buildUpgrades(consumer, "backpack", SophisticatedAccelerator.ACCELERATORS,
                upgradeBase("sophisticatedbackpacks"));
        buildUpgrades(consumer, "storage", SophisticatedAccelerator.ACCELERATORS_STORAGE,
                upgradeBase("sophisticatedstorage"));
    }

    private Item upgradeBase(String namespace) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, "upgrade_base");
        if (!ForgeRegistries.ITEMS.containsKey(id)) {
            throw new IllegalStateException("Missing datagen dependency item: " + id);
        }
        return ForgeRegistries.ITEMS.getValue(id);
    }

    private void buildUpgrades(Consumer<FinishedRecipe> consumer, String folder,
                               Map<AcceleratorTier, RegistryObject<AcceleratorUpgradeItem>> upgrades,
                               Item upgradeBase) {
        specialRecipe(consumer, folder, AcceleratorTier.BASIC, upgrades.get(AcceleratorTier.BASIC).get(),
                upgradeBase, Items.ENDER_EYE, Items.CLOCK);
        materialRecipe(consumer, folder, AcceleratorTier.IRON, upgrades.get(AcceleratorTier.IRON).get(),
                upgrades.get(AcceleratorTier.BASIC).get(), Items.IRON_INGOT, Items.IRON_BLOCK);
        materialRecipe(consumer, folder, AcceleratorTier.GOLD, upgrades.get(AcceleratorTier.GOLD).get(),
                upgrades.get(AcceleratorTier.IRON).get(), Items.GOLD_INGOT, Items.GOLD_BLOCK);
        specialRecipe(consumer, folder, AcceleratorTier.DIAMOND, upgrades.get(AcceleratorTier.DIAMOND).get(),
                upgrades.get(AcceleratorTier.GOLD).get(), Items.DIAMOND, Items.NETHER_STAR);
    }

    private void specialRecipe(Consumer<FinishedRecipe> consumer, String folder, AcceleratorTier tier,
                               Item result, Item previous, Item material, Item center) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
                .pattern("MCM").pattern("RUR").pattern("MRM")
                .define('M', material).define('C', center).define('R', Items.REDSTONE).define('U', previous)
                .unlockedBy("has_previous_upgrade", has(previous))
                .save(consumer, recipeId(folder, tier));
    }

    private void materialRecipe(Consumer<FinishedRecipe> consumer, String folder, AcceleratorTier tier,
                                Item result, Item previous, Item ingot, Item block) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result)
                .pattern("MRM").pattern("RUR").pattern("MRM")
                .define('M', ingot).define('R', block).define('U', previous)
                .unlockedBy("has_previous_upgrade", has(previous))
                .save(consumer, recipeId(folder, tier));
    }

    private ResourceLocation recipeId(String folder, AcceleratorTier tier) {
        return ResourceLocation.fromNamespaceAndPath(SophisticatedAccelerator.MOD_ID,
                folder + "/" + tier.id() + "_accelerator_upgrade");
    }
}
