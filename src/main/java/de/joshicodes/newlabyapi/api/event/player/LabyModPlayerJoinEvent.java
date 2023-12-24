package de.joshicodes.newlabyapi.api.event.player;

import de.joshicodes.newlabyapi.api.LabyModPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class LabyModPlayerJoinEvent extends LabyModPlayerEvent {

    private final String rawJson;

    public LabyModPlayerJoinEvent(LabyModPlayer player, String json) {
        super(player);
        this.rawJson = json;
    }

    public String getRawJson() {
        return rawJson;
    }

    @Override
    public HandlerList getHandlers() {
        return super.getHandlers();
    }

}
