package de.joshicodes.newlabyapi.api;

import de.joshicodes.newlabyapi.LabyModProtocol;
import de.joshicodes.newlabyapi.api.event.player.LabyModPlayerJoinEvent;
import de.joshicodes.newlabyapi.api.object.InputPrompt;
import de.joshicodes.newlabyapi.api.object.LabyProtocolObject;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class LabyModAPI {

    private static final HashMap<Player, LabyModPlayer> labyModPlayers = new HashMap<>();

    public static void sendObject(Player player, String key, LabyProtocolObject obj) {
        LabyModProtocol.sendLabyModMessage(player, key, obj.toJson());
    }

    public static void sendPrompt(Player player, InputPrompt prompt) {
        LabyModProtocol.sendLabyModMessage(player, "input_prompt", prompt.toJson());
    }

    public static LabyModPlayer getLabyModPlayer(Player player) {
        if(!labyModPlayers.containsKey(player))
            return null;
        return labyModPlayers.get(player);
    }

    public static LabyModPlayer getLabyModPlayer(UUID uuid) {
        for(Player player : labyModPlayers.keySet()) {
            if(player.getUniqueId().equals(uuid))
                return labyModPlayers.get(player);
        }
        return null;
    }

    public static void executeLabyModJoin(Player player, String version, String json) {
        LabyModPlayer labyModPlayer = new LabyModPlayer(player, version);
        labyModPlayers.put(player, labyModPlayer);
        LabyModPlayerJoinEvent event = new LabyModPlayerJoinEvent(labyModPlayer, json);
        player.getServer().getPluginManager().callEvent(event);
    }

}
