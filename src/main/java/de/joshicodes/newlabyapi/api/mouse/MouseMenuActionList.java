package de.joshicodes.newlabyapi.api.mouse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.joshicodes.newlabyapi.api.object.LabyProtocolObject;

import java.util.HashMap;

public class MouseMenuActionList implements LabyProtocolObject {

    private final HashMap<String, MouseMenuAction> actions;

    public MouseMenuActionList() {
        this.actions = new HashMap<>();
    }

    /**
     * Adds a new action to the list
     * @param key The display name of the action
     * @param type The type of the action
     * @param value The value of the action
     *              You can use "{name}" as placeholder for the player name
     */
    public void addAction(String key, MouseMenuActionType type, String value) {
        actions.put(key, new MouseMenuAction(type, value));
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }

    public HashMap<String, MouseMenuAction> getActionMap() {
        return actions;
    }

    @Override
    public JsonElement toJson() {
        JsonArray array = new JsonArray();
        actions.forEach((key, action) -> {
            JsonObject actionObject = new JsonObject();
            actionObject.addProperty("displayName", key);
            actionObject.addProperty("type", action.type.name());
            actionObject.addProperty("value", action.value);
            array.add(actionObject);
        });
        return array;
    }

    public record MouseMenuAction(MouseMenuActionType type, String value) { }

}
