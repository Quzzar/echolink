package com.quzzar.echolink.commands;

import java.util.HashSet;

import com.quzzar.echolink.Echolink;
import com.quzzar.echolink.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhisperCommand implements CommandExecutor {

    private static final String permission = "echolink.whisper";

    public static final String commandLayout = "/whisper <message>";

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

                    Echolink.localChatManager.getWhisperProcessor().sendLocalMessage(
                            player,
                            new HashSet<>(Bukkit.getOnlinePlayers()),
                            message.toString()
                    );

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
