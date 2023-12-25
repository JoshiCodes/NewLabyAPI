package de.joshicodes.newlabyapi.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.joshicodes.newlabyapi.LabyModProtocol;
import de.joshicodes.newlabyapi.api.mouse.MouseMenuActionList;
import de.joshicodes.newlabyapi.api.object.InputPrompt;
import de.joshicodes.newlabyapi.api.object.LabyModPlayerSubtitle;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class LabyModPlayer {

    private final Player player;
    private final String clientVersion;

    private LabyModPlayerSubtitle ownSubtitle = null;

    public LabyModPlayer(Player player, String clientVersion) {
        this.player = player;
        this.clientVersion = clientVersion;
    }

    /**
     * Sends the provided subtitles to the player. <br>
     * This will result in only this player seeing the subtitles.
     * @param subtitles The subtitles to send
     */
    public void sendSubtitles(List<LabyModPlayerSubtitle> subtitles) {
        sendSubtitles(subtitles.toArray(new LabyModPlayerSubtitle[0]));
    }

    /**
     * Sends the provided subtitles to the player. <br>
     * This will result in only this player seeing the subtitles.
     * @param subtitles The subtitles to send
     */
    public void sendSubtitles(LabyModPlayerSubtitle... subtitles) {
        JsonArray array = new JsonArray();
        for(LabyModPlayerSubtitle subtitle : subtitles) {
            array.add(subtitle.toJson());
        }
        LabyModProtocol.sendLabyModMessage(player, "account_subtitle", array);
    }

    /**
     * Sets the Subtitle of this Player for all LabyMod Players.
     * @param subtitle The subtitle <br>
     *                 See {@link LabyModPlayerSubtitle} for more information.
     * @throws IllegalArgumentException If the UUID of the subtitle is not the same as the player UUID
     */
    public void setOwnSubtitle(LabyModPlayerSubtitle subtitle) {
        if(!subtitle.getUUID().equals(player.getUniqueId())) {
            throw new IllegalArgumentException("Subtitle UUID must be the same as the player UUID");
        }
        for(LabyModPlayer all : LabyModAPI.getLabyModPlayers()) {
            all.sendSubtitles(subtitle);
        }
        ownSubtitle = subtitle;
    }

    /**
     * Sets the Subtitle of this Player for all LabyMod Players.
     * @param subtitle The subtitle, as String (Minecraft Legacy Colors are supported, e.g. "&amp;a", "&amp;b", etc.)
     * @param size The size of the subtitle
     */
    public void setOwnSubtitle(String subtitle, double size) {
        setOwnSubtitle(new LabyModPlayerSubtitle(player.getUniqueId(), size, subtitle));
    }

    /**
     * Sets the Subtitle of this Player for all LabyMod Players.
     * @param subtitle The subtitle, as String (Minecraft Legacy Colors are supported, e.g. "&amp;a", "&amp;b", etc.)
     */
    public void setOwnSubtitle(String subtitle) {
        setOwnSubtitle(subtitle, 0.8d);
    }

    /**
     * Sets the Subtitle of this Player for all LabyMod Players.
     * @param subtitle The subtitle, as Adventure Component
     */
    public void setOwnSubtitle(Component subtitle) {
        setOwnSubtitle(subtitle, 0.8d);
    }

    /**
     * Sets the Subtitle of this Player for all LabyMod Players.
     * @param subtitle The subtitle, as Adventure Component
     * @param size The size of the subtitle
     */
    public void setOwnSubtitle(Component subtitle, double size) {
        setOwnSubtitle(new LabyModPlayerSubtitle(player.getUniqueId(), size, subtitle));
    }

    /**
     * Clears the Subtitle of this Player for all LabyMod Players.
     */
    public void clearSubtitle() {
        setOwnSubtitle(new LabyModPlayerSubtitle(player.getUniqueId()));
        this.ownSubtitle = null;
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
     * Allows or Denies the client certain Permissions.
     * LabyDocs: <a href="https://docs.labymod.net/pages/server/moderation/permissions/">docs.labymod.net/pages/server/moderation/permissions/</a>
     * @param permissions The permissions to allow or deny
     *                  
     * @see LabyModPermission
     * @see #sendPermissions(List, List) 
     */
    public void sendPermissions(HashMap<LabyModPermission, Boolean> permissions) {
        JsonObject obj = new JsonObject();
        for(LabyModPermission permission : permissions.keySet()) {
            obj.addProperty(permission.name(), permissions.get(permission));
        }
        LabyModProtocol.sendLabyModMessage(player, "PERMISSIONS", obj);
    }

    /**
     * Allows or Denies the client certain Permissions.
     * LabyDocs: <a href="https://docs.labymod.net/pages/server/moderation/permissions/">docs.labymod.net/pages/server/moderation/permissions/</a>
     * @param allow The permissions to allow
     * @param deny The permissions to deny
     *
     * @see LabyModPermission
     * @see #sendPermissions(HashMap)
     */
    public void sendPermissions(List<LabyModPermission> allow, List<LabyModPermission> deny) {
        // Remove any duplicates in allow - Deny takes priority
        allow.removeIf(deny::contains);

        HashMap<LabyModPermission, Boolean> permissions = new HashMap<>();
        for(LabyModPermission perm : allow) {
            permissions.put(perm, true);
        }
        for(LabyModPermission perm : deny) {
            permissions.put(perm, false);
        }
        sendPermissions(permissions);

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

    /**
     * Sends a server banner to the player. <br>
     * LabyDocs: <a href="https://docs.labymod.net/pages/server/displays/tablist/#server-banner">docs.labymod.net/pages/server/displays/tablist/#server-banner</a>
     * @param imageUrl The image url
     *                 Has to be 5:1 ratio, recommended scale is 1280x256
     */
    public void sendServerBanner(String imageUrl) {
        JsonObject object = new JsonObject();
        object.addProperty("url", imageUrl);
        LabyModProtocol.sendLabyModMessage(player, "server_banner", object);
    }

    public Player getPlayer() {
        return player;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public LabyModPlayerSubtitle getOwnSubtitle() {
        return ownSubtitle;
    }

    public boolean hasSubtitle() {
        return ownSubtitle != null;
    }

}
