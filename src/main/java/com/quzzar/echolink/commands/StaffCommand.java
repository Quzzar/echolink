package com.quzzar.echolink.commands;

import com.quzzar.echolink.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffCommand implements CommandExecutor {

    private static final String permission = "echolink.staffchat";
    private static final String channelPrefix = "§8[§cStaff§8]§r";

    public static final String commandLayout = "/staff <message>";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {

        if(sender instanceof Player) {

            Player player = (Player) sender;

            if(player.hasPermission(permission)) {

                if(args.length>0) {

                    StringBuilder message = new StringBuilder();
                    for(int i=0; i<args.length; i++){
                        message.append(args[i]);
                        message.append(" ");
                    }

                    String messageStr = message.toString();

                    for(Player pl : Bukkit.getOnlinePlayers()) {
                        if(pl.hasPermission(permission)) {

                            Utils.sendPlayerMessage(channelPrefix, player, pl, messageStr);

                        }
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
