package com.vomiter.soph_accel.data;

import com.vomiter.soph_accel.SophisticatedAccelerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public final class AcceleratorLanguageProvider extends LanguageProvider {
    private final String locale;

    public AcceleratorLanguageProvider(PackOutput output, String locale) {
        super(output, SophisticatedAccelerator.MOD_ID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        if ("en_us".equals(locale)) {
            add("item.soph_accel.basic_accelerator_upgrade", "Basic Accelerator Upgrade");
            add("item.soph_accel.iron_accelerator_upgrade", "Iron Accelerator Upgrade");
            add("item.soph_accel.gold_accelerator_upgrade", "Gold Accelerator Upgrade");
            add("item.soph_accel.diamond_accelerator_upgrade", "Diamond Accelerator Upgrade");
            add("item.soph_accel.accelerator_upgrade.tooltip", "Accelerates cooker upgrades in the same backpack.");
            add("item.soph_accel.accelerator_upgrade.stats", "Every %s ticks: advances cooking by up to %s ticks.");
            add("item.soph_accel.accelerator_upgrade.accel_fuel", "This accelerator also makes fuel run out faster.");
            add("error.soph_accel.accelerator_exists", "Only one accelerator upgrade can be installed in an inventory.");
            add("item.soph_accel.basic_storage_accelerator_upgrade", "Basic Storage Accelerator Upgrade");
            add("item.soph_accel.iron_storage_accelerator_upgrade", "Iron Storage Accelerator Upgrade");
            add("item.soph_accel.gold_storage_accelerator_upgrade", "Gold Storage Accelerator Upgrade");
            add("item.soph_accel.diamond_storage_accelerator_upgrade", "Diamond Storage Accelerator Upgrade");
        }
        else if ("zh_tw".equals(locale)) {
            add("item.soph_accel.basic_accelerator_upgrade", "基礎加速器升級");
            add("item.soph_accel.iron_accelerator_upgrade", "鐵加速器升級");
            add("item.soph_accel.gold_accelerator_upgrade", "金加速器升級");
            add("item.soph_accel.diamond_accelerator_upgrade", "鑽石加速器升級");
            add("item.soph_accel.accelerator_upgrade.tooltip", "加速同一背包內已註冊加速函式的升級。");
            add("item.soph_accel.accelerator_upgrade.stats", "每 %s tick，最多縮短 %s tick 的料理等待時間。");
            add("item.soph_accel.accelerator_upgrade.accel_fuel", "這個加速升級也會導致燃料更快耗盡");
            add("error.soph_accel.accelerator_exists", "每個存儲空間只能安裝一個加速器升級。");
            add("item.soph_accel.basic_storage_accelerator_upgrade", "基礎儲物加速器升級");
            add("item.soph_accel.iron_storage_accelerator_upgrade", "鐵儲物加速器升級");
            add("item.soph_accel.gold_storage_accelerator_upgrade", "金儲物加速器升級");
            add("item.soph_accel.diamond_storage_accelerator_upgrade", "鑽石儲物加速器升級");
        }
    }
}
