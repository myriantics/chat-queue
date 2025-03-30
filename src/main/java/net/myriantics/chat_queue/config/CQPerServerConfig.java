package net.myriantics.chat_queue.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CQPerServerConfig {
    // Explosive Enhancement's config was used as an example
    // https://github.com/Superkat32/Explosive-Enhancement
    // Very awesome mod, would highly recommend
    // Makes explosions look awesome :D

    public String serverIp;
    public boolean isEnabled;

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static Path PATH;

    @Nullable
    public static CQPerServerConfig test(Path testedPath) {
        CQPerServerConfig config = null;
        File file = testedPath.toFile();

        if(file.exists()) {
            try (BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            )) {
                config = GSON.fromJson(fileReader, CQPerServerConfig.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load AutoViewBobbing Config :(", e);
            }
        }

        if(config != null) {
            config.save(testedPath);
        }

        return config;

    }

    public void save(Path path) {
        try (Writer writer = Files.newBufferedWriter(
                path, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
