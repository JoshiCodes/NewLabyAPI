package de.joshicodes.newlabyapi.api.event;

import de.joshicodes.newlabyapi.api.LabyModPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class InputPromptEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final LabyModPlayer player;
    private final int id;
    private final String value;

    public InputPromptEvent(LabyModPlayer player, int id, String value) {
        this.player = player;
        this.id = id;
        this.value = value;
    }

    public LabyModPlayer getPlayer() {
        return player;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
