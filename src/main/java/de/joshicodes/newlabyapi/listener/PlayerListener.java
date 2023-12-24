package de.joshicodes.newlabyapi.listener;

import de.joshicodes.newlabyapi.api.LabyModAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        LabyModAPI.executeLabyModQuit(e.getPlayer());
    }

}
