package xyz.cecchetti.clearunsentmessages;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.VarClientStr;
import net.runelite.api.widgets.Widget;
import net.runelite.client.input.KeyManager;

import javax.inject.Inject;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ChatMessageManager {

    private static final Pattern PATTERN_ONLY_SPACES_AND_NUMBERS = Pattern.compile("^[ |\\d]*$");
    private static final Pattern PATTERN_CHATBOX_TEXT_PREFIX = Pattern.compile("(.*?): <col=(.*?)>");
    private static final String CHATBOX_TEXT_EMPTY_MESSAGE_FORMAT = "%s: <col=%s></col><col=%<s>*</col>";
    private static final Component STUB_COMPONENT = new Component() {};

    private final AtomicReference<Widget> chatboxWidgetHolder = new AtomicReference<>();

    private final Client client;
    private final ClearUnsentConfig config;
    private final KeyManager keyManager;
    private final ScheduledExecutorService executorService;

    private ScheduledFuture<?> future = null;

    @Inject
    ChatMessageManager(
            Client client,
            ClearUnsentConfig config,
            KeyManager keyManager,
            ScheduledExecutorService executorService
    ) {
        this.client = client;
        this.config = config;
        this.keyManager = keyManager;
        this.executorService = executorService;
    }

    public void onMessageChanged() {
        Optional.ofNullable(future).ifPresent(f -> f.cancel(true));
        future = executorService.schedule(() -> {
            final String typedText = client.getVarcStrValue(VarClientStr.CHATBOX_TYPED_TEXT);
            if (!shouldClearMessage(typedText)) {
                log.debug("No update needed: [" + typedText + "]");
                return;
            }
            log.debug("(executed) Clearing: [" + typedText + "]");
            clearMessage();
            sendBackspace();
        }, config.delay(), TimeUnit.MILLISECONDS);
    }

    public void releaseChatboxWidget() {
        chatboxWidgetHolder.set(null);
    }

    private boolean shouldClearMessage(String msg) {
        return !config.onlySpacesNumbers() || PATTERN_ONLY_SPACES_AND_NUMBERS.matcher(msg).matches();
    }

    private void clearMessage() {
        // This changes what value the client is going to send when you press enter
        client.setVarcStrValue(VarClientStr.CHATBOX_TYPED_TEXT, "");

        // This changes what value you _see_ down in the chatbox
        Widget chatboxWidget = chatboxWidgetHolder.updateAndGet(w -> w != null ? w : client.getWidget(162, 55));
        if (chatboxWidget == null) {
            return;
        }
        final String widgetText = chatboxWidget.getText();
        if (widgetText == null) {
            return;
        }
        Matcher matcher = PATTERN_CHATBOX_TEXT_PREFIX.matcher(widgetText);
        if (!matcher.find()) {
            return;
        }
        chatboxWidget.setText(String.format(CHATBOX_TEXT_EMPTY_MESSAGE_FORMAT, matcher.group(1), matcher.group(2)));
        chatboxWidget.setXTextAlignment(0);
    }

    private void sendBackspace() {
        // Fixes compatibility with the Key Remapping plugin, causing it to lock the chat after we clear it
        KeyEvent fakeKeyEvent = new KeyEvent(STUB_COMPONENT, 1, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE,
                (char) KeyEvent.VK_BACK_SPACE);
        keyManager.processKeyPressed(fakeKeyEvent);
        keyManager.processKeyReleased(fakeKeyEvent);
        keyManager.processKeyTyped(fakeKeyEvent);
    }

}
