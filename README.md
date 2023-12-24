# NewLabyAPI
### This is a simple Labymod API for LabyMod 4 for Paper 1.20.2

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
### InputPrompt
You can send an InputPrompt to the client, which will ask the player for an input.
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
Modify the Middle Click Menu of the player.
[More Information on the LabyMod Docs](https://docs.labymod.net/pages/server/labymod/action_menu/)
````java
MouseMenuActionList menu = new MouseMenuActionList();
menu.addAction(
        "Report player",
        MouseMenuActionType.SUGGEST_COMMAND,
        "/report {player}"
);
// Send to player
labyPlayer.sendActionMenu(menu);
````

## Server Banner
Sends a Server Banner to the client.
[More Information on the LabyMod Docs](https://docs.labymod.net/pages/server/displays/tablist/#server-banner)
The Banner has to have a ratio of 5:1, recommended size is 1280x256px.
````java
labyPlayer.sendServerBanner("https://example.com/banner.png");
````