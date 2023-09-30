package com.quzzar.echolink.localchat;

import java.util.Set;
import java.util.SplittableRandom;

import com.quzzar.echolink.Echolink;
import com.quzzar.echolink.Utils;
import net.kyori.adventure.audience.Audience;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatProcessor {

    private SplittableRandom splitRand;;

    private String prefix;
    private double baseMax;
    private double extRange;

    private double M;
    private double B;

    public ChatProcessor(String prefix, double baseMax, double extRange) {

        this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        this.baseMax = baseMax;
        this.extRange = extRange;

        this.M = -1/(extRange*2);
        this.B = -(baseMax+extRange)*M;

        this.splitRand = new SplittableRandom();

    }

    public String getPrefix() {
        return prefix;
    }

    private double getChance(double distance) {
        return M*distance+B;
    }

    private String concealMessage(double distance, String message) {

        double chance = getChance(distance);

        StringBuilder strBuilder = new StringBuilder();

        for(char c : message.toCharArray()) {
            // Always include spaces
            if(c == ' ') {
                strBuilder.append(c);
            } else {

                // If the chance permits, add character
                if(splitRand.nextDouble()<chance) {
                    strBuilder.append(c);
                } else {
                    if(LocalChatManager.randomForMissing) {
                        strBuilder.append("§k-§r");
                    } else {
                        strBuilder.append("§m §r");
                    }
                }

            }

        }

        if(Echolink.DEBUG_MODE) {
            // Display chance of concealment and distance the receiver is from the transmitter
            return strBuilder.toString() + " §e§o( "+(int)(getChance(distance)*100)+"% : "+(int)distance+" )";
        }

        return strBuilder.toString();

    }

    public void sendLocalMessage(Player p, Set<Player> recipients, String sendingMessage) {

        boolean sent = false;
        for(Player rp : recipients) {

            // If the players are in the same world
            if(p.getWorld().equals(rp.getWorld())) {

                double distance = p.getLocation().distance(rp.getLocation());

                if(distance > baseMax+extRange) {
                    // 0% success. Do nothing, don't send message
                } else if(distance < baseMax-extRange) {
                    // 100% success, just send normal
                    Utils.sendPlayerMessage(prefix, p, rp, sendingMessage);
                    if(rp != p) {sent = true;}
                } else {
                    // Conceal message...
                    Utils.sendPlayerMessage(prefix, p, rp, concealMessage(distance, sendingMessage));
                    if(rp != p) {sent = true;}
                }

            }

        }

        if(!sent) {
            if(Echolink.DEBUG_MODE) {
                p.sendMessage(ChatColor.YELLOW+""+ChatColor.ITALIC+"It seems like no one is around to hear you, maybe you could use shout.");
            }
        }

    }

}
