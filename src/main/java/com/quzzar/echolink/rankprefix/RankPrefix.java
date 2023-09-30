package com.quzzar.echolink.rankprefix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class RankPrefix {

    private final String prefix;
    private final String permission;

    public RankPrefix(String name, String prefix) {

        this.permission = "echolink.prefix."+name;
        this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);

        Bukkit.getPluginManager().addPermission(new Permission(permission, PermissionDefault.FALSE));

    }

    public String getPrefix() {
        return prefix;
    }

    public String getPermission() {
        return permission;
    }

}
