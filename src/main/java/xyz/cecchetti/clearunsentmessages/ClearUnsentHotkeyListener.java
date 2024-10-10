package xyz.cecchetti.clearunsentmessages;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Singleton;

@Slf4j
@Singleton
public class ClearUnsentHotkeyListener extends HotkeyListener {

    private final ClearUnsentConfig config;
    private final ChatMessageManager chatMessageManager;

    @Inject
    private ClearUnsentHotkeyListener(
            ClearUnsentConfig config,
            ChatMessageManager chatMessageManager
    ) {
        super(config::clearHotkey);
        this.config = config;
        this.chatMessageManager = chatMessageManager;
    }

    @Override
    public void hotkeyPressed() {
        if (!config.enableClearHotkey()) {
            return;
        }
        log.debug("Hot key pressed, clearing any unsent message");
        chatMessageManager.clearMessage();
    }
}
