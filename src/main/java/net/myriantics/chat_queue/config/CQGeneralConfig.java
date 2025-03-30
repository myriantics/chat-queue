package net.myriantics.chat_queue.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.myriantics.chat_queue.ChatQueueClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CQGeneralConfig {

    // Explosive Enhancement's config was used as an example
    // https://github.com/Superkat32/Explosive-Enhancement
    // Very awesome mod, would highly recommend
    // Makes explosions look awesome :D

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(ChatQueueClient.MOD_ID + "_global.json");
    public static File file = PATH.toFile();
    public static CQGeneralConfig INSTANCE = load();

    public boolean modEnabled = true;
    public boolean modPaused = false;

    public static CQGeneralConfig load() {
        CQGeneralConfig config = null;
        if(file.exists()) {
            try (BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            )) {
                config = GSON.fromJson(fileReader, CQGeneralConfig.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load AutoViewBobbing Config :(", e);
            }
        }

        if(config == null) {
            config = new CQGeneralConfig();
        }

        config.save();
        return config;

    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(
                PATH, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
