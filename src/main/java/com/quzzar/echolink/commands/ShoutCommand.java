package com.quzzar.echolink.commands;

import com.quzzar.echolink.Echolink;
import com.quzzar.echolink.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ShoutCommand implements CommandExecutor {

    private static final String permission = "echolink.shout";

    public static final String commandLayout = "/shout <message>";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;

            if(player.hasPermission(permission)) {

                if(args.length>0) {

                    StringBuilder messageBuilder = new StringBuilder();
                    for (String arg : args) {
                        messageBuilder.append(arg);
                        messageBuilder.append(" ");
                    }
                    String message = messageBuilder.toString();

                    boolean showToEveryone = Echolink.plugin.getConfig().getBoolean("local_chat.shout.show_to_everyone");

                    if (showToEveryone) {
                        for(Player rp : Bukkit.getOnlinePlayers()) {
                            Utils.sendPlayerMessage(
                                    Echolink.localChatManager.getShoutProcessor().getPrefix(),
                                    player,
                                    rp,
                                    message
                            );
                        }
                    } else {
                        Echolink.localChatManager.getShoutProcessor().sendLocalMessage(
                                player,
                                new HashSet<>(Bukkit.getOnlinePlayers()),
                                message
                        );
                    }

                } else {
                    player.sendMessage(ChatColor.RED+commandLayout);
                }

            } else {
                Utils.needsPermission(player);
            }

        }

        return true;
    }

}
