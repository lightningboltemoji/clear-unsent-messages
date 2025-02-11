package xyz.cecchetti.clearunsentmessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.VarClientStr;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.input.KeyManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
@Singleton
public class ChatMessageManager {

    private static final Pattern PATTERN_CHATBOX_TEXT_PREFIX = Pattern.compile("(.*?): <col=(.*?)>");
    private static final String CHATBOX_TEXT_EMPTY_MESSAGE_FORMAT = "%s: <col=%s></col><col=%<s>*</col>";

    private final AtomicReference<Widget> chatboxWidgetHolder = new AtomicReference<>();

    private final Client client;
    private final ClientThread clientThread;
    private final KeyManager keyManager;

    public void clearMessage() {
        clientThread.invokeLater(() -> {
            // This changes what value the client is going to send when you press enter
            client.setVarcStrValue(VarClientStr.CHATBOX_TYPED_TEXT, "");

            // This changes what value you _see_ down in the chatbox
            Widget chatboxWidget = chatboxWidgetHolder.updateAndGet(w -> w != null ? w : client.getWidget(
                    ComponentID.CHATBOX_INPUT));
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

            sendBackspace();
        });
    }

    private void sendBackspace() {
        // Fixes compatibility with the Key Remapping plugin, causing it to lock the chat after we clear it
        KeyEvent fakeKeyEvent = new KeyEventFakeBackspace();
        keyManager.processKeyPressed(fakeKeyEvent);
        keyManager.processKeyReleased(fakeKeyEvent);
        keyManager.processKeyTyped(fakeKeyEvent);
    }

    public void releaseChatboxWidget() {
        chatboxWidgetHolder.set(null);
    }

}
