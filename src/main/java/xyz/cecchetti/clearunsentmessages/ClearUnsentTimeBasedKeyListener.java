package xyz.cecchetti.clearunsentmessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.VarClientStr;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.input.KeyListener;

import javax.inject.Inject;
import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
public class ClearUnsentTimeBasedKeyListener implements KeyListener {

    private static final Pattern PATTERN_ONLY_SPACES_AND_NUMBERS = Pattern.compile("^[ |\\d]*$");

    // Pressing these keys won't cause the timer to reset
    private static final Set<Integer> KEY_CODE_IGNORED = Set.of(
            KeyEvent.VK_UP,
            KeyEvent.VK_DOWN,
            KeyEvent.VK_LEFT,
            KeyEvent.VK_RIGHT,
            KeyEvent.VK_CONTROL,
            KeyEvent.VK_ALT,
            KeyEvent.VK_META,
            KeyEvent.VK_SHIFT
    );

    private final Client client;
    private final ClientThread clientThread;
    private final ChatMessageManager chatMessageManager;
    private final ClearUnsentConfig config;
    private final ScheduledExecutorService executorService;

    private ScheduledFuture<?> future;

    @Override
    public void keyPressed(KeyEvent e) {
        if (!config.enableTimeBased() || KEY_CODE_IGNORED.contains(e.getKeyCode()) || e instanceof KeyEventFakeBackspace) {
            return;
        }
        log.debug("Key pressed - checking back in {}ms if message should be cleared", config.delay());
        Optional.ofNullable(future).ifPresent(f -> f.cancel(true));
        future = executorService.schedule(this::maybeClearMessage, config.delay(), TimeUnit.MILLISECONDS);
    }

    private void maybeClearMessage() {
        clientThread.invokeLater(() -> {
            final String typedText = client.getVarcStrValue(VarClientStr.CHATBOX_TYPED_TEXT);
            if (!shouldClearMessage(typedText)) {
                log.debug("No update needed: [{}]", typedText);
                return;
            }
            log.debug("(executed) Clearing: [{}]", typedText);
            chatMessageManager.clearMessage();
        });
    }

    private boolean shouldClearMessage(String msg) {
        return !config.onlySpacesNumbers() || PATTERN_ONLY_SPACES_AND_NUMBERS.matcher(msg).matches();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}