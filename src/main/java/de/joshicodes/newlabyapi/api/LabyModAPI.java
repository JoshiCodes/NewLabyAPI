package de.joshicodes.newlabyapi.api;

import com.google.gson.JsonObject;
import de.joshicodes.newlabyapi.LabyModProtocol;
import de.joshicodes.newlabyapi.NewLabyPlugin;
import de.joshicodes.newlabyapi.api.event.player.LabyModPlayerJoinEvent;
import de.joshicodes.newlabyapi.api.object.InputPrompt;
import de.joshicodes.newlabyapi.api.object.LabyModPlayerSubtitle;
import de.joshicodes.newlabyapi.api.object.LabyProtocolObject;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LabyModAPI {

    private static final HashMap<Player, LabyModPlayer> labyModPlayers = new HashMap<>();

    private static boolean updateExistingPlayersOnJoin = false;

    public static void init(NewLabyPlugin plugin) {
        updateExistingPlayersOnJoin = plugin.getConfig().getBoolean("updateExistingPlayersOnJoin", true);
    }

    public static void sendObject(Player player, String key, LabyProtocolObject obj) {
        LabyModProtocol.sendLabyModMessage(player, key, obj.toJson());
    }

    public static void sendPrompt(Player player, InputPrompt prompt) {
        LabyModProtocol.sendLabyModMessage(player, "input_prompt", prompt.toJson());
    }

    public static LabyModPlayer getLabyModPlayer(Player player) {
        if (!labyModPlayers.containsKey(player))
            return null;
        return labyModPlayers.get(player);
    }

    public static LabyModPlayer getLabyModPlayer(UUID uuid) {
        for (Player player : labyModPlayers.keySet()) {
            if (player.getUniqueId().equals(uuid))
                return labyModPlayers.get(player);
        }
        return null;
    }

    public static void executeLabyModJoin(Player player, String version, String json) {
        LabyModPlayer labyModPlayer = new LabyModPlayer(player, version);
        labyModPlayers.put(player, labyModPlayer);
        LabyModPlayerJoinEvent event = new LabyModPlayerJoinEvent(labyModPlayer, json);
        player.getServer().getPluginManager().callEvent(event);

        // Update existing players (if enabled)
        if (!updateExistingPlayersOnJoin) return;
        NewLabyPlugin.debug("Updating existing players for " + player.getName() + " (" + player.getUniqueId() + ")");
        new BukkitRunnable() {
            @Override
            public void run() {
                List<LabyModPlayerSubtitle> subtitles = new ArrayList<>();
                for (LabyModPlayer all : labyModPlayers.values()) {
                    if (all.equals(labyModPlayer))
                        continue;
                    if (all.hasSubtitle()) {
                        subtitles.add(all.getOwnSubtitle());
                        NewLabyPlugin.debug("Found subtitle for " + all.getPlayer().getName() + " (" + all.getPlayer().getUniqueId() + ")");
                    }
                }
                if (!subtitles.isEmpty())
                    labyModPlayer.sendSubtitles(subtitles);
            }
        }.runTaskLater(NewLabyPlugin.getInstance(), 2L);

    }

    public static void executeLabyModQuit(Player player) {
        labyModPlayers.remove(player);
    }

    public static List<LabyModPlayer> getLabyModPlayers() {
        return new ArrayList<>(labyModPlayers.values());
    }

}
