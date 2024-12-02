# NewLabyAPI
### This is a simple Labymod API for LabyMod 4 for Paper 1.20.2

> [!Note]
> 
> There now seems to be a [new official LabyMod Server API](https://github.com/LabyMod/labymod4-server-api) for LabyMod 4. <br>
> This plugin will no longer be updated, as the official API should be the better choice.

<br>

- [Why?](#why)
- [Download & Install](#download)
    
<b>API</b>
- [Maven](#maven)
- [How to use](#how-to-use)
- [Events](#events)
- [Included LabyMod Protocol Objects](#included-labymod-protocol-objects)

## Why?
As you may already know, there is [an official LabyMod Server API](https://github.com/LabyMod/labymod-server-api). So why create another one?
It seems that the original does not work with current Versions of minecraft and is also made to work with LabyMod 3 (it should still work with Laby 4 tho).
As far as i can tell, there is [another third-party LabyMod Server API](https://www.spigotmc.org/resources/labymod-server-api-1-8-1-17-1.92724/), but this one still does not seem to work with 1.20.2.
I do hope that LabyMod creates a new Server Plugin for their new API and keeps it up to date, until then, my go-to solution will be this repository.

## Download
To use this API you need the plugin jar File on your Server.
You can download the latest file [here](https://github.com/JoshiCodes/NewLabyAPI/releases).

To use this API in your Plugin, you need to add it as a dependency.
### Maven
````xml
<repository>
    <id>joshicodes-de-releases</id>
    <name>JoshiCodes Repository</name>
    <url>https://repo.joshicodes.de/releases</url>
</repository>

<dependency>
    <groupId>de.joshicodes</groupId>
    <artifactId>NewLabyAPI</artifactId>
    <version>1.2.5</version>
    <scope>provided</scope>
</dependency>
````

## How to use:
The [Protocol Util Class](https://docs.labymod.net/pages/server/protocol/protocol/#utils-class), mentioned in the LabyMod Documentation,
is already implemented and can be used standalone.

````java
final String key = "some_key_here";
final JsonElement data = new JsonObject();
LabyModProtocol.sendLabyMessage(player, key, data);
````

Alternative, you can use the provided classes to send data to the client.
You can send every ``LabyProtocolObject`` to the client, via `LabyModAPI.sendObject(Player, String, LabyProtocolObject)`, where the String is the key of the object.

Some classes, like ``InputPrompt`` have their own Methods to send them to the client:
``LabyModAPI.sendPrompt(Player, InputPrompt)``
Some objects even have their own method in the ``LabyModPlayer`` class.
You can get a LabyModPlayer using ``LabyModAPI.getLabyModPlayer(Player)`` or ``LabyModAPI.getLabyModPlayer(UUID)``.

## Events
This API provides some events, which listen to LabyMod specific events.
You can listen to those events exactly like every other Bukkit Event.
At the moment there are the following events:
- ``LabyModPlayerJoinEvent`` for when a LabyMod player joins the server
- ``InputPromptEvent`` for when a player answers an InputPrompt

## Included LabyMod Protocol Objects

- [InputPrompt](#inputprompt)
- [MouseMenuActionList](#mousemenuactionlist)
- [Server Banner](#server-banner)
- [Permissions](#permissions)
- [PlayerSubtitle](#subtitles)

### InputPrompt
You can send an InputPrompt to the client, which will ask the player for an input. <br>
[More Information on the LabyMod Docs](https://docs.labymod.net/pages/server/minecraft/input_prompt/)
````java
// ID (int, unique for each player), Message (String, shown to the player), Value (String, default value), Placeholder (String, placeholder)
final InputPrompt prompt = new InputPrompt(1234, "What is your name?", "<Name>", "<Name>");
LabyModAPI.sendPrompt(player, prompt);
// Or use LabyModPlayer
LabyModPlayer labyPlayer = ...
labyPlayer.sendInputPrompt(prompt);
````

## MouseMenuActionList
Modify the Middle Click Menu of the player. <br>
[More Information on the LabyMod Docs](https://docs.labymod.net/pages/server/labymod/action_menu/)
````java
MouseMenuActionList menu = new MouseMenuActionList();
menu.addAction(
        "Report player",
        MouseMenuActionType.SUGGEST_COMMAND,
        "/report {name}"
);
// Send to player
labyPlayer.sendActionMenu(menu);
````

## Server Banner
Sends a Server Banner to the client. <br>
[More Information on the LabyMod Docs](https://docs.labymod.net/pages/server/displays/tablist/#server-banner)
The Banner has to have a ratio of 5:1, recommended size is 1280x256px.
````java
labyPlayer.sendServerBanner("https://example.com/banner.png");
````

## Permissions
[LabyMod Docs](https://docs.labymod.net/pages/server/moderation/permissions/) <br>
Allows or Denies certain permissions.
````java
labyPlayer.sendPermissions(HashMap<LabyModPermission, Boolean>);
// or
labyPlayer.sendPermission(List<LabyModPermission> allow, List<LabyModPermission> deny);
````
There is already a way implemented to send those permissions on player join.<br>
To use them, just set ``permissions.use`` to ``true`` in the config.yml. <br>
After that, you can enable or disable the permissions in the config.yml.

## Subtitles
[LabyMod Docs](https://docs.labymod.net/pages/server/displays/subtitles/) <br>
Displays a Subtitle to everyone or just a specific player.
````java
labyPlayer.sendSubtitle(LabyModPlayerSubtitle...); // Sends the Subtitles to just this player
labyPlayer.setSubtitle(LabyModPlayerSubtitle); // Sets the subtitle of the player. This updates the subtitle for everyone.
labyPlayer.clearSubtitle(); // Clears the subtitle of the player. This updates the subtitle for everyone.

````

<br><br><br>

# Possible Deprecated, broken or not implemented features
### These features are not tested and may not work as expected.
### It is possible those features are not available in the current version of LabyMod.
### The Documentation of those features still exists on the LabyMod Docs.

- [Playing Gamemode](#playing-gamemode)

## Playing Gamemode
[Labymod Docs](https://docs.labymod.net/pages/server/labymod/gamemode/) <br>
Displays the current playing gamemode to the player.<br>
````java
labyPlayer.setCurrentPlayingGamemode("Gamemode");
````

<br><br><br>

## Contribute
Feel free to contribute in any way you like, just create a pull request or just create an issue.
I will try to keep an eye on everything, but may take some time to respond.
