package de.joshicodes.newlabyapi;

import de.joshicodes.newlabyapi.api.LabyModAPI;
import de.joshicodes.newlabyapi.listener.ConfigListener;
import de.joshicodes.newlabyapi.listener.LabyPluginMessageListener;
import de.joshicodes.newlabyapi.listener.PlayerListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public final class NewLabyPlugin extends JavaPlugin {

    public static final Component PREFIX = MiniMessage.miniMessage().deserialize(
            "<b><gradient:#227eb8:#53bbfb>NewLabyAPI</gradient></b> <gray>»</gray>"
    );

    private static NewLabyPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        saveDefaultConfig();

        LabyModAPI.init(this);

        getServer().getMessenger().registerIncomingPluginChannel(this, "labymod3:main", new LabyPluginMessageListener());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "labymod3:main");

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), this);

        pluginManager.registerEvents(new ConfigListener(getConfig()), this);

        if (getConfig().getBoolean("updateChecks.enabled", true)) {
            final NewLabyPlugin instance = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    getLogger().info("Checking for updates...");
                    String version = getDescription().getVersion();
                    if (version.contains("-dev")) {
                        // Don't check for updates if the version is a dev version
                        getLogger().info("You are running a dev version, so no update check will be performed!");
                        return;
                    }
                    UpdateChecker updateChecker = new UpdateChecker(instance);
                    try {
                        updateChecker.checkForUpdate();
                    } catch (IOException e) {
                        getLogger().warning("Could not check for updates!");
                        return;
                    }
                    if (!updateChecker.isUpToDate()) {
                        final String url = "https://github.com/JoshiCodes/NewLabyAPI/releases/latest";
                        final String current = getDescription().getVersion();
                        getLogger().info(" ");
                        getLogger().info(" ");
                        getLogger().info("There is a new version available! (" + updateChecker.getLatestVersion() + ")");
                        getLogger().info("You are running:" + current);
                        getLogger().info("Download it here: " + url);
                        if (getConfig().getBoolean("updateChecks.joinNotify", true)) {
                            final String permission = getConfig().getString("updateChecks.joinNotifyPerm", "newlabyapi.update");
                            pluginManager.registerEvents(new Listener() {
                                @EventHandler
                                public void onJoin(PlayerJoinEvent e) {
                                    if (e.getPlayer().hasPermission(permission)) {
                                        e.getPlayer().sendMessage(Component.space());
                                        e.getPlayer().sendMessage(
                                                Component.join(
                                                        JoinConfiguration.separator(Component.space()),
                                                        PREFIX,
                                                        MiniMessage.miniMessage().deserialize(
                                                                "<gradient:#227eb8:#53bbfb>There is a new version available! (" + updateChecker.getLatestVersion() + ")</gradient>"
                                                        )
                                                )
                                        );
                                        e.getPlayer().sendMessage(
                                                Component.join(
                                                        JoinConfiguration.separator(Component.space()),
                                                        PREFIX,
                                                        MiniMessage.miniMessage().deserialize(
                                                                "<gradient:#227eb8:#53bbfb>You are running:</gradient> <b><color:#53bbfb>" + current + "</color></b>"
                                                        )
                                                )
                                        );
                                        e.getPlayer().sendMessage(
                                                Component.join(
                                                        JoinConfiguration.separator(Component.space()),
                                                        PREFIX,
                                                        MiniMessage.miniMessage().deserialize(
                                                                "<gradient:#227eb8:#53bbfb>Download it here:</gradient> <b><color:#53bbfb><click:open_url:'" + url + "'>" + url + "</click></color></b>"
                                                        )
                                                )
                                        );
                                    }
                                }
                            }, instance);
                        }
                    } else {
                        getLogger().info("You are running the latest version!");
                    }
                }
            }.runTaskLater(this, 20);
        }

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
        if (instance.getConfig().getBoolean("debug")) {
            instance.getLogger().info("[DEBUG] " + s);
        }
    }

}
