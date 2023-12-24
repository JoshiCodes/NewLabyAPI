package de.joshicodes.newlabyapi.listener;

import de.joshicodes.newlabyapi.LabyModProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class LabyModListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] message) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        ByteBuf buf = Unpooled.wrappedBuffer(message);
        String key = LabyModProtocol.readString(buf, Short.MAX_VALUE);
        String json = LabyModProtocol.readString(buf, Short.MAX_VALUE);

        System.out.println("Received message from LabyMod: " + key + " " + json);

        // LabyMod user joins the server
        if(key.equals("INFO")) {
            // Handle the json message
            System.out.println("LabyMod user " + json + " joined the server!");
        }
    }
}
