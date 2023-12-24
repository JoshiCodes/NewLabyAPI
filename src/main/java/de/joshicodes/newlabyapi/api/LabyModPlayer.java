package de.joshicodes.newlabyapi.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.joshicodes.newlabyapi.LabyModProtocol;
import de.joshicodes.newlabyapi.api.mouse.MouseMenuActionList;
import de.joshicodes.newlabyapi.api.object.InputPrompt;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class LabyModPlayer {

    private final Player player;
    private final String clientVersion;

    public LabyModPlayer(Player player, String clientVersion) {
        this.player = player;
        this.clientVersion = clientVersion;
    }

    /**
     * @deprecated Seems not to work in LabyMod 4.
     * Sends a new Server Banner to the player. <br>
     * LabyDocs: <a href="https://docs.labymod.net/pages/server/labymod/gamemode/">docs.labymod.net/pages/server/labymod/gamemode/</a>
     * @param visible If the gamemode should be visible
     * @param gamemode The gamemode name
     */
    @Deprecated
    public void setCurrentPlayingGamemode(boolean visible, String gamemode) {
        JsonObject object = new JsonObject();
        object.addProperty("show_gamemode", visible);
        object.addProperty("gamemode_name", gamemode);
        LabyModProtocol.sendLabyModMessage(player, "server_gamemode", object);
    }

    /**
     * Sends a new Action Menu to the player. <br>
     * LabyDocs: <a href="https://docs.labymod.net/pages/server/labymod/action_menu/">docs.labymod.net/pages/server/labymod/action_menu/</a>
     * @param list The Action Menu, see {@link MouseMenuActionList}
     *             for more information.
     */
    public void sendActionMenu(MouseMenuActionList list) {
        JsonArray array = list.toJson().getAsJsonArray();
        LabyModProtocol.sendLabyModMessage(player, "user_menu_actions", array);
    }

    /**
     * Sends a new Input Prompt to the player. <br>
     * LabyDocs: <a href="https://docs.labymod.net/pages/server/minecraft/input_prompt/">docs.labymod.net/pages/server/minecraft/input_prompt/</a>
     * @param prompt
     */
    public void sendInputPrompt(InputPrompt prompt) {
        LabyModAPI.sendPrompt(player, prompt);
    }

    public Player getPlayer() {
        return player;
    }

    public String getClientVersion() {
        return clientVersion;
    }

}
