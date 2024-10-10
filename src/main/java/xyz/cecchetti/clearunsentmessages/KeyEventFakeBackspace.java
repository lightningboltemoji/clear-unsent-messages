package xyz.cecchetti.clearunsentmessages;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyEventFakeBackspace extends KeyEvent {

    private static final Component STUB_COMPONENT = new Component() {};

    public KeyEventFakeBackspace() {
        super(STUB_COMPONENT, 1, System.currentTimeMillis(), 0, KeyEvent.VK_BACK_SPACE,
                (char) KeyEvent.VK_BACK_SPACE);
    }
}
