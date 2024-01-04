package de.joshicodes.newlabyapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateChecker {

    private final String url = "https://api.github.com/repos/JoshiCodes/NewLabyAPI/releases/latest";

    private final NewLabyPlugin plugin;

    private String latestVersion;

    UpdateChecker(NewLabyPlugin plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdate() throws IOException {
        // Make GET request to GitHub API
        URL url = new URL(this.url);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
        } catch (Exception e) {
            plugin.getLogger().warning("Could not check for updates!");
            return;
        }
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setInstanceFollowRedirects(true);
        connection.connect();
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        JsonObject response = new Gson().fromJson(reader, JsonObject.class);
        if(response == null) {
            plugin.getLogger().warning("Could not check for updates!");
            return;
        }
        if(!response.has("tag_name")) {
            plugin.getLogger().warning("Update Check failed, as the response does not contain a tag_name!");
            return;
        }
        String tagName = response.get("tag_name").getAsString();
        if(tagName == null) {
            plugin.getLogger().warning("Update Check failed, as the response does not contain a tag_name!");
            return;
        }
        if(tagName.startsWith("v"))
            tagName = tagName.replaceFirst("v", "");
        latestVersion = tagName;
    }

    public String getLatestVersion() {
        if(latestVersion == null) {
            try {
                checkForUpdate();
            } catch (IOException e) {
                plugin.getLogger().warning("Could not check for updates!");
                plugin.getLogger().warning("Error: " + e.getMessage());
                return plugin.getDescription().getVersion();
            }
        }
        return latestVersion;
    }

    public boolean isUpToDate() {
        return plugin.getDescription().getVersion().equals(getLatestVersion());
    }

}
