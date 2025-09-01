package xyz.cecchetti.clearunsentmessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ScriptID;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarClientID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.input.KeyManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.event.KeyEvent;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
@Singleton
public class ChatMessageManager {
    private final Client client;
    private final ClientThread clientThread;
    private final KeyManager keyManager;

    public void clearMessage() {
        clientThread.invokeLater(() -> {
            // Changes what value the client is going to send when you press enter
            client.setVarcStrValue(VarClientID.CHATINPUT, "");

            // Changes what value you see down in the chatbox
            client.runScript(ScriptID.CHAT_PROMPT_INIT);

            // For compatibility with the Key Remapping plugin, to cause the chat to lock. We only want to send this when
            // the chatbox is focused, as otherwise, it could actually backspace a character -- see issue #6.
            if (isChatboxFocused()) {
                sendBackspace();
            }
        });
    }

    private boolean isChatboxFocused() {
        // See https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/keyremapping/KeyRemappingPlugin.java
        final Widget chatboxParent = client.getWidget(InterfaceID.Chatbox.UNIVERSE);
        if (chatboxParent == null || chatboxParent.getOnKeyListener() == null) {
            return false;
        }
        final Widget worldMapSearch = client.getWidget(InterfaceID.Worldmap.MAPLIST_DISPLAY);
        if (worldMapSearch != null && client.getVarcIntValue(VarClientID.WORLDMAP_SEARCHING) == 1) {
            return false;
        }
        final Widget report = client.getWidget(InterfaceID.Reportabuse.UNIVERSE);
        return report == null;
    }

    private void sendBackspace() {
        // Fixes compatibility with the Key Remapping plugin, causing it to lock the chat after we clear it
        final KeyEvent fakeKeyEvent = new KeyEventFakeBackspace();
        keyManager.processKeyPressed(fakeKeyEvent);
        keyManager.processKeyReleased(fakeKeyEvent);
        keyManager.processKeyTyped(fakeKeyEvent);
    }

}
