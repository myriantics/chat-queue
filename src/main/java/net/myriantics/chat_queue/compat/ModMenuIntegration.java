package net.myriantics.chat_queue.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.myriantics.chat_queue.ChatQueueClient;
import net.myriantics.chat_queue.config.YACLIntegration;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (ChatQueueClient.isYaclLoaded()) {
            return YACLIntegration::makeScreen;
        }
        return ModMenuApi.super.getModConfigScreenFactory();
    }
}
