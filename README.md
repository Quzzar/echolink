![Echolink](https://i.imgur.com/JxlbNpv.png "Echolink")

> <em>Overhaul chat with proximity-based communication and an echolink item to connect with others on the same frequency. Stay in sync and strategize effortlessly.</em>
---
### Overview
Utilize Echolinks for discreet communication with friends sharing your frequency. The Echolink recipe is shapeless; arranging the ingredients in different sequences creates unique frequencies—so safeguard your recipe!

Local chat is enabled by default, with fully configurable ranges. As you distance yourself from others, their messages will naturally become less clear and harder to decipher.

<em>Everything is fully configurable and toggleable (including item texture). Check the ``config.yml`` for details.</em>

### Recipe <em>(configurable)</em>
![Echolink Item Recipe](https://i.imgur.com/begI2FO.jpg "Echolink Item Recipe")
<br />
<em>Amethyst shard, lightning rod, echo shard, nautilus shell, block of copper, emerald, redstone dust, iron bars, redstone torch.</em>
<br />
<br />
This is a shapeless recipe but <b>the ingredient order determines the Echolink's channel frequency.</b>
<br />
Only Echolinks with the same frequency can communicate with each other <em>so protect your recipe order!</em> 

### How It Works
When you send a message while an Echolink is in your hot bar, offhand, or on your head, it will send that message through the Echolink's channel instead.
Anyone who also has an Echolink of that same frequency anywhere in their inventory will receive your message.

You can send messages through multiple Echolink channels at the same time, just put them in your hot bar!

### Commands
 - ``/shout`` or ``/s``: Say your message loudly, increasing the range of people who can hear you!
 - ``/whisper`` or ``/w``: Say your message quietly, only people nearby can hear.
 - ``/tell <player>``: DMs a specific player a message, op only by default
 - ``/staff``: Say your message in the global staff channel, op by default

### Permissions

 - ``echolink.shout``: Allows usage of /shout command
 - ``echolink.whisper``: Allows usage of /whisper command
 - ``echolink.tell``: Allows usage of /tell command, op only by default
 - ``echolink.staffchat``: Access to the staff channel and usage of /staff command, op only by default
 - ``echolink.prefix.<RANK>``: Gives the indicated rank prefix, see config.yml for details

---
<sup>This is a rework of my original plugin, [RPChat](https://github.com/Quzzar/Minecraft-Plugins/tree/main/RPChat).</sup>
