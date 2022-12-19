package xyz.cecchetti.clearunsentmessages;

import com.google.inject.Inject;
import com.google.inject.Provides;
import net.runelite.api.VarClientStr;
import net.runelite.api.events.VarClientStrChanged;
import net.runelite.api.events.WidgetClosed;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(name = "Clear Unsent Messages")
public class ClearUnsentPlugin extends Plugin {

	@Inject
	private ChatMessageManager chatMessageManager;

	@Provides
	ClearUnsentConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ClearUnsentConfig.class);
	}

	@Subscribe
	public void onWidgetClosed(WidgetClosed widgetClosed) {
		if (widgetClosed.getGroupId() == 162) {
			chatMessageManager.releaseChatboxWidget();
		}
	}

	@Subscribe
	public void onVarClientStrChanged(VarClientStrChanged strChanged) {
		if (strChanged.getIndex() == VarClientStr.CHATBOX_TYPED_TEXT) {
			chatMessageManager.onMessageChanged();
		}
	}

}
