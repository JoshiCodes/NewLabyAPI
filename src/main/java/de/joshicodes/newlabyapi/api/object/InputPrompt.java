package de.joshicodes.newlabyapi.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class InputPrompt implements LabyProcotolObject {

    private int id;
    private String message;
    private String value;
    private String placeholder;
    private int maxLength;

    /**
     * @param id A unique id for each packet, use a static number and increase it for each prompt request
     * @param message The message above the text field
     * @param value The value inside of the text field
     * @param placeholder A placeholder text inside of the text field if there is no value given
     * @param maxLength Max amount of characters of the text field value
     */
    public InputPrompt(final int id, String message, String value, String placeholder, int maxLength) {
        this.id = id;
        this.message = message;
        this.value = value;
        this.placeholder = placeholder;
        this.maxLength = maxLength;
    }

    public InputPrompt setMessage(String message) {
        this.message = message;
        return this;
    }

    public InputPrompt setValue(String value) {
        this.value = value;
        return this;
    }

    public InputPrompt setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public InputPrompt setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("id", id);
        if(message != null)
            object.addProperty("message", message);
        if(value != null)
            object.addProperty("value", value);
        if(placeholder != null)
            object.addProperty("placeholder", placeholder);
        if(maxLength > 0)
            object.addProperty("max_length", maxLength);
        return object;
    }

}
