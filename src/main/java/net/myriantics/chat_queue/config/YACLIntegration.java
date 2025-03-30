package net.myriantics.chat_queue.config;

import dev.isxander.yacl3.api.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.myriantics.chat_queue.ChatQueueClient;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class YACLIntegration {

    public static Screen makeScreen(Screen parent) {
        HashMap<Path, CQPerServerConfig> loadedPerServerConfigs = getServerConfigFiles();
        CQPerServerConfig global = new CQGlobalDefaultConfig();
        CQPerServerConfig defaults = new CQPerServerConfig();

        var yacl = YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("chat_queue.title"));

        var generalCategoryBuilder = ConfigCategory.createBuilder()
                .name(Text.translatable("chat_queue.general.category"));

        var queueCategoryBuilder = ConfigCategory.createBuilder()
                .name(Text.translatable("chat_queue.queues.category"));

        var globalSettingsGroup = OptionGroup.createBuilder()
                .name(Text.translatable("chat_queue.queues.settings.global"))
                .description(OptionDescription.of(Text.translatable("chat_queue.queues.settings.global.description")));

        var button = ButtonOption.createBuilder()
                .name(Text.translatable("chat_queue.queues.settings.global.button"))
                .description(OptionDescription.of(Text.translatable("chat_queue.queues.settings.global.button.description")))
                .action(((yaclScreen, buttonOption) -> openScreen(makeSubScreen(global, parent))));

        var perServerSettingsGroup = ConfigCategory.createBuilder()
                .name(Text.translatable("chat_queue.per_server.category"));

        globalSettingsGroup.option(button.build());

        queueCategoryBuilder.group(globalSettingsGroup.build());

        yacl.category(queueCategoryBuilder.build());

        return yacl.build().generateScreen(parent);
    }

    private static void openScreen(Screen screen) {
        MinecraftClient.getInstance().setScreen(screen);
    }

    private static Screen makeSubScreen(CQPerServerConfig config, Screen parent) {
        var yacl = YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("chat_queue.title"));

        var specificQueueCategoryBuilder = ConfigCategory.createBuilder()
                .name(Text.translatable("chat_queue.specific_queue.settings"));

        var setting = OptionGroup.createBuilder().name(Text.literal("sigma"));

        var text = LabelOption.create(Text.literal("skib test"));
        /*
        var queues = ListOption.<PrefixedChatQueue>createBuilder()
                .name(Text.translatable("chat_que"))
                */

        setting.option(text);

        specificQueueCategoryBuilder.group(setting.build());

        yacl.category(specificQueueCategoryBuilder.build());

        return yacl.build().generateScreen(parent);
    }
    /*
    private static ButtonOption createButtonOption(@Nullable CQPerServerConfig config) {
        if (config == null) config = new CQPerServerConfig();

        return ButtonOption.createBuilder()
                .name(Text.literal(config.serverIp))
                .description(Text.translatable());
    }
     */

    private static HashMap<Path, CQPerServerConfig> getServerConfigFiles() {
        HashMap<Path, CQPerServerConfig> configs = new HashMap<>();

        // only let json files through
        try (Stream<Path> potentialConfigPathStream = Files.list(getConfigDirectoryPath()).filter((path -> path.endsWith(".json")))) {
            List<Path> potentialConfigPaths = potentialConfigPathStream.toList();

            for (Path testPath : potentialConfigPaths) {
                @Nullable CQPerServerConfig config = CQPerServerConfig.test(testPath);
                if (config != null) {
                    configs.put(testPath, config);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return configs;
    }

    public static Path getFilePath(String serverId) {
        return getConfigDirectoryPath().resolve("/" + serverId + ".json");
    }

    private static Path getConfigDirectoryPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(ChatQueueClient.MOD_ID);
    }
}
