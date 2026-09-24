package com.autocraft.mod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * AutoCraft ayarlarini tutar ve diske (config/autocraft.json) kaydeder.
 */
public class AutoCraftConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("autocraft.json");

    /** Otomatik craft edilecek itemlarin ID listesi, orn: "minecraft:stick" */
    public List<String> selectedItems = new ArrayList<>();

    /** Recipe book'taki "hepsini craft et" (shift-click) davranisiyla ayni */
    public boolean craftAll = true;

    /** Craft edilen itemlar icin chat'e bilgi mesaji bas */
    public boolean chatFeedback = true;

    /** Modul acik mi */
    public boolean active = false;

    /** Kac tick'te bir craft istegi gonderilsin (5 = saniyede 4 kez) */
    public int intervalTicks = 5;

    public static AutoCraftConfig load() {
        if (Files.exists(PATH)) {
            try (Reader reader = Files.newBufferedReader(PATH)) {
                AutoCraftConfig cfg = GSON.fromJson(reader, AutoCraftConfig.class);
                if (cfg != null) {
                    return cfg;
                }
            } catch (IOException ignored) {
                // dosya bozuksa varsayilan ayarlarla devam et
            }
        }
        return new AutoCraftConfig();
    }

    public void save() {
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException ignored) {
        }
    }
}
