package de.joshicodes.newlabyapi.api.object;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

import javax.annotation.Nullable;
import java.util.UUID;

public class LabyModPlayerSubtitle implements LabyProtocolObject {

    private final UUID uuid;
    private double size;
    private @Nullable String value;
    private @Nullable Component valueComponent;

    public LabyModPlayerSubtitle(final UUID uuid) {
        this.uuid = uuid;
        this.value = null;
        this.size = 0.8d;
    }

    public LabyModPlayerSubtitle(final UUID uuid, final double size, final @Nullable String value) {
        this.uuid = uuid;
        this.value = value;
        setSize(size);
    }

    public LabyModPlayerSubtitle(final UUID uuid, final double size, final @Nullable Component valueComponent) {
        this.uuid = uuid;
        this.valueComponent = valueComponent;
        setSize(size);
    }

    /**
     * Set the size of the subtitle<br>
     * This should be between 0.8 and 1.6.
     * @param size The size of the subtitle
     * @throws IllegalArgumentException if the size is not between 0.8 and 1.6
     * @return The current instance
     */
    public LabyModPlayerSubtitle setSize(double size) {
        if(size < 0.8 || size > 1.6) throw new IllegalArgumentException("Size must be between 0.8 and 1.6");
        this.size = size;
        return this;
    }

    /**
     * Set the subtitle text. <br>
     * Set to null to remove the subtitle.
     * @param value The subtitle text or null
     * @return The current instance
     */
    public LabyModPlayerSubtitle setValue(@Nullable String value) {
        this.value = value;
        return this;
    }

    /**
     * Set the subtitle text. <br>
     * Set to null to remove the subtitle.
     * @param value The subtitle text or null
     * @return The current instance
     */
    public LabyModPlayerSubtitle setValue(@Nullable Component value) {
        this.valueComponent = value;
        return this;
    }

    public UUID getUUID() {
        return uuid;
    }

    public double getSize() {
        return size;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    @Nullable
    public Component getValueComponent() {
        return valueComponent;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("uuid", uuid.toString());
        object.addProperty("size", size);
        if(valueComponent != null)
            object.addProperty("raw_json_text", JSONComponentSerializer.json().serialize(valueComponent));
        else if(value != null)
            object.addProperty("value", value);
        return object;
    }

}
