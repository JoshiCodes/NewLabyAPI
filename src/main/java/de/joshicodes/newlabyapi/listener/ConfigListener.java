package de.joshicodes.newlabyapi.listener;

import de.joshicodes.newlabyapi.NewLabyPlugin;
import de.joshicodes.newlabyapi.api.LabyModPermission;
import de.joshicodes.newlabyapi.api.event.player.LabyModPlayerJoinEvent;
import de.joshicodes.newlabyapi.api.mouse.MouseMenuActionList;
import de.joshicodes.newlabyapi.api.mouse.MouseMenuActionType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ConfigListener implements Listener {

    private final FileConfiguration config;

    public ConfigListener(@NotNull FileConfiguration config) {
        this.config = config;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onLabyJoin(LabyModPlayerJoinEvent event) {
        if (config.getBoolean("permissions.use", false)) {
            HashMap<LabyModPermission, Boolean> permissions = new HashMap<>();
            for (LabyModPermission permission : LabyModPermission.values()) {
                permissions.put(permission, config.getBoolean("permissions.list." + permission.name(), permission.isDefaultEnabled()));
            }
            event.getPlayer().sendPermissions(permissions);
        }
        if (config.getBoolean("serverBanner.use", false))
            event.getPlayer().sendServerBanner(config.getString("serverBanner.url"));
        if (config.getBoolean("extendActionMenu.use", false)) {
            MouseMenuActionList list = new MouseMenuActionList();
            for (Map<?, ?> map : config.getMapList("extendActionMenu.list")) {
                NewLabyPlugin.debug("Found action menu entry!");
                if (!map.containsKey("display") || !map.containsKey("value") || !map.containsKey("action"))
                    continue;
                NewLabyPlugin.debug("Display: " + map.get("display") + ", Value: " + map.get("value") + ", Action: " + map.get("action"));
                if (!(map.get("display") instanceof String display) || !(map.get("value") instanceof String value) || !(map.get("action") instanceof String actionString))
                    continue;
                if (map.containsKey("requiresPermission") && map.get("requiresPermission") instanceof String permission) {
                    if (!event.getPlayer().getPlayer().hasPermission(permission))
                        continue;
                }
                NewLabyPlugin.debug("Passed permission check and parsed values!");
                MouseMenuActionType action;
                try {
                    action = MouseMenuActionType.valueOf(actionString.toUpperCase());
                } catch (IllegalArgumentException e) {
                    NewLabyPlugin.debug("Invalid action type: " + actionString);
                    continue;
                }
                list.addAction(display, action, value);
            }
            NewLabyPlugin.debug("Action menu list size: " + list.getActionMap().size());
            if (!list.isEmpty())
                event.getPlayer().sendActionMenu(list);
        }
    }

}
