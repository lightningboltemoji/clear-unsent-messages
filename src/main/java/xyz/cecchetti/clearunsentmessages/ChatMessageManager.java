package xyz.cecchetti.clearunsentmessages;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.VarClientStr;
import net.runelite.api.widgets.Widget;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

@Slf4j
public class ChatMessageManager {

    private static final Pattern PATTERN_ONLY_SPACES_AND_NUMBERS = Pattern.compile("^[ |\\d]+$");

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
        final String cleaned = cleanMessage(typedText);

        // If we don't have any changes to make, no need to set the timer
        if (typedText.equals(cleaned)) {
            log.debug("No update needed: [" + typedText + "]");
            return;
        }

        log.debug("(scheduled) Before: [" + typedText + "], After: [" + cleaned + "]");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.debug("(executed) Before: [" + typedText + "], After: [" + cleaned + "]");
                setMessage(cleaned);
            }
        }, config.delay());
    }

    public void releaseChatboxWidget() {
        chatboxWidget = null;
    }

    private String cleanMessage(String msg) {
        return !config.onlySpacesNumbers() || PATTERN_ONLY_SPACES_AND_NUMBERS.matcher(msg).matches() ? "" : msg;
    }

    private void setMessage(String msg) {
        // This changes what value the client is going to send when you press enter
        client.setVarcStrValue(VarClientStr.CHATBOX_TYPED_TEXT, msg);

        // This changes what value you _see_ down in the chatbox
        final String widgetText = chatboxWidget.getText();
        if (widgetText != null) {
            chatboxWidget.setText(widgetText.replaceFirst("<col=0000ff>.*</col><col=0000ff>\\*</col>",
                    "<col=0000ff>" + msg + "</col><col=0000ff>\\*</col>"));
        }
    }

}
