package net.myriantics.chat_queue.api;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.myriantics.chat_queue.config.CQPerServerConfig;

public class CQSubScreen extends Screen {
    private final CQPerServerConfig config;
    protected CQSubScreen(Text title, CQPerServerConfig config) {
        super(title);
        this.config = config;
    }
}
