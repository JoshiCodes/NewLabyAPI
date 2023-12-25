package de.joshicodes.newlabyapi.listener;

import de.joshicodes.newlabyapi.api.LabyModPermission;
import de.joshicodes.newlabyapi.api.event.player.LabyModPlayerJoinEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PermissionListener implements Listener {

    private final FileConfiguration config;

    public PermissionListener(@NotNull FileConfiguration config) {
        this.config = config;
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onLabyJoin(LabyModPlayerJoinEvent event) {
        if(!config.getBoolean("permissions.use", false)) return; // Should never happen, but just in case
        HashMap<LabyModPermission, Boolean> permissions = new HashMap<>();
        for(LabyModPermission permission : LabyModPermission.values()) {
            permissions.put(permission, config.getBoolean("permissions.list." + permission.name(), permission.isDefaultEnabled()));
        }
        event.getPlayer().sendPermissions(permissions);
    }

}
