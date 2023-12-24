package de.joshicodes.newlabyapi.api.event.player;

import de.joshicodes.newlabyapi.api.LabyModPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LabyModPlayerEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final LabyModPlayer player;

    public LabyModPlayerEvent(LabyModPlayer player) {
        this.player = player;
    }

    public LabyModPlayer getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
