package xyz.cecchetti.clearunsentmessages;

import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.WidgetClosed;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(name = "Clear Unsent Messages")
public class ClearUnsentPlugin extends Plugin {

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private ClearUnsentTimeBasedKeyListener timeBasedKeyListener;

	@Inject
	private ClearUnsentHotkeyListener hotkeyListener;

	@Inject
	private KeyManager keyManager;

	@Provides
	ClearUnsentConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ClearUnsentConfig.class);
	}

	@Override
	protected void startUp() throws Exception {
		keyManager.registerKeyListener(timeBasedKeyListener);
		keyManager.registerKeyListener(hotkeyListener);
	}

	@Override
	protected void shutDown() throws Exception {
		keyManager.unregisterKeyListener(timeBasedKeyListener);
		keyManager.unregisterKeyListener(hotkeyListener);
	}

	@Subscribe
	public void onWidgetClosed(WidgetClosed widgetClosed) {
		if (widgetClosed.getGroupId() == 162) {
			chatMessageManager.releaseChatboxWidget();
		}
	}

}
