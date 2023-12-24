package de.joshicodes.newlabyapi.listener;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.joshicodes.newlabyapi.LabyModProtocol;
import de.joshicodes.newlabyapi.NewLabyPlugin;
import de.joshicodes.newlabyapi.api.LabyModAPI;
import de.joshicodes.newlabyapi.api.LabyModPlayer;
import de.joshicodes.newlabyapi.api.event.InputPromptEvent;
import de.joshicodes.newlabyapi.api.event.player.LabyModPlayerJoinEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.UUID;

public class LabyPluginMessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] message) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        ByteBuf buf = Unpooled.wrappedBuffer(message);
        String key = LabyModProtocol.readString(buf, Short.MAX_VALUE);
        String json = LabyModProtocol.readString(buf, Short.MAX_VALUE);
        JsonObject obj = new Gson().fromJson(json, JsonObject.class);

        NewLabyPlugin.debug("Received message from LabyMod: " + key + " " + json);

        // LabyMod user joins the server
        if(key.equals("INFO")) {
            // Handle the json message
            String version = obj.get("version").getAsString();
            LabyModAPI.executeLabyModJoin(player, version, json);
        } else if(key.equals("input_prompt")) {
            // json contains "id" and "value"
            LabyModPlayer labyModPlayer = LabyModAPI.getLabyModPlayer(player);
            if(labyModPlayer != null) {
                InputPromptEvent event = new InputPromptEvent(labyModPlayer, obj.get("id").getAsInt(), obj.get("value").getAsString());
                NewLabyPlugin.getInstance().getServer().getPluginManager().callEvent(event);
            }
        }

    }

    /**
     * Add a secret key to the json object
     * @param jsonObject json object
     * @param hasKey has key name
     * @param key key name
     * @param secret the secret key
     * @return json object
     */
    public JsonObject addSecret(JsonObject jsonObject, String hasKey, String key, UUID secret, String domain) {
        jsonObject.addProperty( hasKey, true );
        jsonObject.addProperty( key, secret.toString() + ":" + domain );
        return jsonObject;
    }

}
