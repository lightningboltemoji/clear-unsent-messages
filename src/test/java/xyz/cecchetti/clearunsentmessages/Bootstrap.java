package xyz.cecchetti.clearunsentmessages;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class Bootstrap {

    /**
     * Starts RuneLite with the plugin loaded to aid in development
     * @param args
     */
    public static void main(String[] args) {
        ExternalPluginManager.loadBuiltin(ClearUnsentPlugin.class);
        try {
            RuneLite.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
