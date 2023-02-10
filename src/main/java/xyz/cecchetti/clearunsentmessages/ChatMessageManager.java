package xyz.cecchetti.clearunsentmessages;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.VarClientStr;
import net.runelite.api.widgets.Widget;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ChatMessageManager {

    private static final Pattern PATTERN_ONLY_SPACES_AND_NUMBERS = Pattern.compile("^[ |\\d]+$");
    private static final Pattern PATTERN_CHATBOX_TEXT_PREFIX = Pattern.compile("(.*?): <col=(.*?)>");
    private static final String CHATBOX_TEXT_EMPTY_MESSAGE_FORMAT = "%s: <col=%s></col><col=%<s>*</col>";

    private Client client;
    private ClearUnsentConfig config;

    private Widget chatboxWidget = null;
    private Timer timer = null;

    @Inject
    ChatMessageManager(Client client, ClearUnsentConfig config) {
        this.client = client;
        this.config = config;
    }

    public void onMessageChanged() {
        // Grab chatbox widget if we don't have it already
        if (chatboxWidget == null) {
            chatboxWidget = client.getWidget(162, 55);
        }

        // Cancel any pending chat changes
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        // Determine current text and if it needs to change
        final String typedText = client.getVarcStrValue(VarClientStr.CHATBOX_TYPED_TEXT);
        if (!shouldClearMessage(typedText)) {
            log.debug("No update needed: [" + typedText + "]");
            return;
        }

        log.debug("(scheduled) Before: [" + typedText + "]");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.debug("(executed) Before: [" + typedText + "]");
                clearMessage();
            }
        }, config.delay());
    }

    public void releaseChatboxWidget() {
        chatboxWidget = null;
    }

    private boolean shouldClearMessage(String msg) {
        return !config.onlySpacesNumbers() || PATTERN_ONLY_SPACES_AND_NUMBERS.matcher(msg).matches();
    }

    private void clearMessage() {
        // This changes what value the client is going to send when you press enter
        client.setVarcStrValue(VarClientStr.CHATBOX_TYPED_TEXT, "");

        // This changes what value you _see_ down in the chatbox
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

}
