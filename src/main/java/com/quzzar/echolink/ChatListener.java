package com.quzzar.echolink;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {
        if(!(e.message() instanceof TextComponent)){ return; }

        Player p = e.getPlayer();
        TextComponent message = ((TextComponent) e.message());

        boolean itemEnabled = Echolink.plugin.getConfig().getBoolean("echolink_item.enabled");
        if(itemEnabled) {
            List<ItemStack> echolinks = Utils.getPlayerEcholinks(p);
            if (!echolinks.isEmpty()) {
                e.setCancelled(true);

                // Only send message to people who share an echolink
                List<Player> recipients = Utils.getFreqRecipients(echolinks);
                for (Player rp : recipients) {
                    List<ItemStack> rpInv = Utils.getUniqueItems(Arrays.asList(rp.getInventory().getContents()));
                    rpInv.retainAll(echolinks);

                    // Send message
                    StringBuilder strBuilder = new StringBuilder();
                    boolean first = true;
                    for (ItemStack echolink : rpInv) {
                        String freq = Utils.getFreqFromItem(echolink);
                        if (freq == null) continue;
                        if (first) {
                            strBuilder.append(freq);
                            first = false;
                        } else {
                            strBuilder.append("§r §7& ").append(freq);
                        }
                    }

                    Utils.sendPlayerMessage(strBuilder.toString(), p, rp, message.content());
                }

                return; // If we have an echolink in inv, don't send local message
            }
        }

        boolean localChatEnabled = Echolink.plugin.getConfig().getBoolean("local_chat.enabled");
        if (localChatEnabled) {
            e.setCancelled(true);

            Set<Player> recipients = new HashSet<>();
            for (Audience audience : e.viewers()){
                if(audience instanceof Player){
                    recipients.add((Player) audience);
                }
            }

            // Send message over local chat
            Echolink.localChatManager.getNormalProcessor().sendLocalMessage(p, recipients, message.content());
        }

    }

}
