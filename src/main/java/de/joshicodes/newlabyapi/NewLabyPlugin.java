package de.joshicodes.newlabyapi;

import de.joshicodes.newlabyapi.api.LabyModAPI;
import de.joshicodes.newlabyapi.listener.LabyPluginMessageListener;
import de.joshicodes.newlabyapi.listener.PlayerListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NewLabyPlugin extends JavaPlugin {

    private static NewLabyPlugin instance;


    @Override
    public void onEnable() {
        instance = this;

        if(!getDataFolder().exists())
            getDataFolder().mkdir();
        saveDefaultConfig();

        LabyModAPI.init(this);

        getServer().getMessenger().registerIncomingPluginChannel(this, "labymod3:main", new LabyPluginMessageListener());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "labymod3:main");

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), this);

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
