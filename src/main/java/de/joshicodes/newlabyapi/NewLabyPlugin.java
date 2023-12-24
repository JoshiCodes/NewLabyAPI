package de.joshicodes.newlabyapi;

import de.joshicodes.newlabyapi.listener.LabyPluginMessageListener;
import de.joshicodes.newlabyapi.listener.TestListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class NewLabyAPI extends JavaPlugin {

    private static NewLabyAPI instance;


    @Override
    public void onEnable() {
        instance = this;

        if(!getDataFolder().exists())
            getDataFolder().mkdir();
        saveDefaultConfig();

        getServer().getMessenger().registerIncomingPluginChannel(this, "labymod3:main", new LabyPluginMessageListener());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "labymod3:main");

        // Test
        getServer().getPluginManager().registerEvents(new TestListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static NewLabyAPI getInstance() {
        return instance;
    }

    public static void debug(String s) {
        NewLabyAPI instance = NewLabyAPI.getInstance();
        if(instance.getConfig().getBoolean("debug")) {
            instance.getLogger().info("[DEBUG] " + s);
        }
    }

}
