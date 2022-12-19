package xyz.cecchetti.clearunsentmessages;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("cfg")
public interface ClearUnsentConfig extends Config {
	@ConfigItem(
		keyName = "delay",
		name = "Delay",
		description = "After this period, any unsent message will cleared"
	)
	default int delay() {
		return 1500;
	}

	@ConfigItem(
			keyName = "onlySpacesNumbers",
			name = "Only spaces/numbers",
			description = "If checked, only clears unsent messages made up of spaces and numbers. If unchecked, clears any unsent message."
	)
	default boolean onlySpacesNumbers() {
		return false;
	}
}
