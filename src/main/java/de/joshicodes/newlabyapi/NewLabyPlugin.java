package de.joshicodes.newlabyapi;

import de.joshicodes.newlabyapi.listener.LabyPluginMessageListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class NewLabyPlugin extends JavaPlugin {

    private static NewLabyPlugin instance;


    @Override
    public void onEnable() {
        instance = this;

        if(!getDataFolder().exists())
            getDataFolder().mkdir();
        saveDefaultConfig();

        getServer().getMessenger().registerIncomingPluginChannel(this, "labymod3:main", new LabyPluginMessageListener());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "labymod3:main");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static NewLabyPlugin getInstance() {
        return instance;
    }

    public static void debug(String s) {
        NewLabyPlugin instance = NewLabyPlugin.getInstance();
        if(instance.getConfig().getBoolean("debug")) {
            instance.getLogger().info("[DEBUG] " + s);
        }
    }

}
