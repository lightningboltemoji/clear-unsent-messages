package xyz.cecchetti.clearunsentmessages;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@ConfigGroup("clear-unsent-messages")
public interface ClearUnsentConfig extends Config {

    @ConfigSection(
            name = "Time-based",
            description = "Settings for clearing unsent messages after a period typing inactivity",
            position = 0
    )
    String timeBasedSection = "timeBased";

    @ConfigSection(
            name = "Hotkey",
            description = "Settings for clearing unsent messages with a hotkey",
            position = 1
    )
    String hotkeySection = "hotkey";

    @ConfigItem(
            keyName = "enableTimeBased",
            name = "Enable time-based",
            description = "Clear unsent messages after a period of typing inactivity",
            section = timeBasedSection,
            position = 0
    )
    default boolean enableTimeBased() {
        return true;
    }

    @ConfigItem(
            keyName = "delay",
            name = "Delay (milliseconds)",
            description = "Period of inactivity after which unsent messages are cleared",
            section = timeBasedSection,
            position = 1
    )
    default int delay() {
        return 2500;
    }

    @ConfigItem(
            keyName = "onlySpacesNumbers",
            name = "Spaces & numbers only",
            description = "Clear only when message is made up of spaces and numbers",
            section = timeBasedSection,
            position = 2
    )
    default boolean onlySpacesNumbers() {
        return false;
    }

    @ConfigItem(
            keyName = "enableClearHotkey",
            name = "Enable hotkey",
            description = "Whether to listen for the hotkey",
            section = hotkeySection,
            position = 0
    )
    default boolean enableClearHotkey() {
        return false;
    }

    @ConfigItem(
            keyName = "clearHotkey",
            name = "Instant clear hotkey",
            description = "Any unsent messages will be immediately cleared when pressed",
            section = hotkeySection,
            position = 1
    )
    default Keybind clearHotkey() {
        return new Keybind(KeyEvent.VK_D, InputEvent.META_DOWN_MASK);
    }
}
