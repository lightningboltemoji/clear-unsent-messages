package xyz.cecchetti.clearunsentmessages;

import net.runelite.api.Client;
import net.runelite.api.VarClientStr;
import net.runelite.api.widgets.Widget;
import net.runelite.client.input.KeyManager;
import org.junit.Test;

import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This is definitely not an exact science, but it should give a rough idea of how well the on-game-thread portion is
 * performing, which will help gauge what impact this plugin has on render performance.
 */
public class Benchmark {

    @Test
    public void benchmark() {
        final Client client = mock(Client.class);
        when(client.getVarcStrValue(VarClientStr.CHATBOX_TYPED_TEXT)).thenReturn("beep boop");

        final Widget chatBox = mock(Widget.class);
        when(client.getWidget(162, 55)).thenReturn(chatBox);

        final ClearUnsentConfig clearUnsentConfig = mock(ClearUnsentConfig.class);
        final KeyManager keyManager = mock(KeyManager.class);

        final ChatMessageManager underTest = new ChatMessageManager(client, clearUnsentConfig, keyManager,
                Executors.newSingleThreadScheduledExecutor());

        // Warm up
        for (int i = 0; i < 100000; i++) {
            underTest.onMessageChanged();
        }

        // Timed iterations
        final long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            underTest.onMessageChanged();
        }
        System.out.println("Took: " + (System.currentTimeMillis() - startTime));
    }
}
