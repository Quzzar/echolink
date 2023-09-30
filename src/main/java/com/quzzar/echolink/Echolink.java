package com.quzzar.echolink;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.quzzar.echolink.commands.ShoutCommand;
import com.quzzar.echolink.commands.StaffCommand;
import com.quzzar.echolink.commands.TellCommand;
import com.quzzar.echolink.commands.WhisperCommand;
import com.quzzar.echolink.localchat.LocalChatManager;
import com.quzzar.echolink.rankprefix.RankPrefixManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public final class Echolink extends JavaPlugin {

    private FileConfiguration config;
    private File configFile;
    public static Echolink plugin;
    public static LocalChatManager localChatManager;
    public static RankPrefixManager rankPrefixManager;
    public static boolean DEBUG_MODE;

    @Override
    public void onEnable() {
        plugin = this;
        printLogo();

        loadConfig();
        UnicodeHandler.initUnicodeCharacters();

        DEBUG_MODE = Echolink.plugin.getConfig().getBoolean("debug_mode");

        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        if(Echolink.plugin.getConfig().getBoolean("staff_chat")){
            getCommand("staff").setExecutor(new StaffCommand());
        }
        if(Echolink.plugin.getConfig().getBoolean("tell_command")){
            getCommand("tell").setExecutor(new TellCommand());
        }

        // Rank prefixes are always enabled
        rankPrefixManager = new RankPrefixManager();

        // Handle echolink item
        boolean itemEnabled = Echolink.plugin.getConfig().getBoolean("echolink_item.enabled");
        if (itemEnabled) {
            getServer().getPluginManager().registerEvents(new RecipeListener(), this);
            registerShapelessRecipe();
            printAmountOfFrequencies();
        }

        // Handle local chat
        boolean localChatEnabled = Echolink.plugin.getConfig().getBoolean("local_chat.enabled");
        if (localChatEnabled) {
            localChatManager = new LocalChatManager();

            if(Echolink.plugin.getConfig().getBoolean("local_chat.shout.enabled")){
                getCommand("shout").setExecutor(new ShoutCommand());
                getCommand("s").setExecutor(new ShoutCommand());
            }
            if(Echolink.plugin.getConfig().getBoolean("local_chat.whisper.enabled")){
                getCommand("whisper").setExecutor(new WhisperCommand());
                getCommand("w").setExecutor(new WhisperCommand());
            }
        }

    }

    @Override
    public void onDisable() {

        //saveConfig();

    }


    private void registerShapelessRecipe() {
        // Create a shapeless recipe with your item as the result
        NamespacedKey key = NamespacedKey.fromString("echolink-recipe");
        if (key == null) return;
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(key, Utils.getEcholinkItemFrame());

        List<String> ingredientList = Echolink.plugin.getConfig().getStringList("echolink_item.shapeless_recipe");

        if(ingredientList.size() > 9 || ingredientList.isEmpty()){
            Utils.tellConsole(ChatColor.RED+"Invalid ingredient list, must have between 1 and 9 ingredients!");
            return;
        }

        for(String ingredient : ingredientList){
            Material mat = Material.getMaterial(ingredient);
            if(mat != null){
                shapelessRecipe.addIngredient(mat);
            } else {
                Utils.tellConsole(ChatColor.RED+"Invalid ingredient: "+ingredient);
                return;
            }
        }

        // Register the recipe
        Bukkit.getServer().addRecipe(shapelessRecipe);

    }


    public void loadConfig() {
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public @NotNull FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("Failed to save config", e.getMessage());
        }
    }

    private void printLogo() {

        Bukkit.getConsoleSender().sendMessage("§5┌§b    ______     __          ___       __  §5┐");
        Bukkit.getConsoleSender().sendMessage("§b    / ____/____/ /_  ____  / (_)___  / /__");
        Bukkit.getConsoleSender().sendMessage("§b   / __/ / ___/ __ \\/ __ \\/ / / __ \\/ //_/");
        Bukkit.getConsoleSender().sendMessage("§b  / /___/ /__/ / / / /_/ / / / / / / ,<");
        Bukkit.getConsoleSender().sendMessage("§b /_____/\\___/_/ /_/\\____/_/_/_/ /_/_/|_|");
        Bukkit.getConsoleSender().sendMessage("§5└-----------------------------------------┘");

        // Old logo
//        Bukkit.getConsoleSender().sendMessage("§5==============================================================");
//        Bukkit.getConsoleSender().sendMessage("§b███████╗ ██████╗██╗  ██╗ ██████╗ ██╗     ██╗███╗   ██╗██╗  ██╗");
//        Bukkit.getConsoleSender().sendMessage("§b██╔════╝██╔════╝██║  ██║██╔═══██╗██║     ██║████╗  ██║██║ ██╔╝");
//        Bukkit.getConsoleSender().sendMessage("§b█████╗  ██║     ███████║██║   ██║██║     ██║██╔██╗ ██║█████╔╝");
//        Bukkit.getConsoleSender().sendMessage("§b██╔══╝  ██║     ██╔══██║██║   ██║██║     ██║██║╚██╗██║██╔═██╗");
//        Bukkit.getConsoleSender().sendMessage("§b███████╗╚██████╗██║  ██║╚██████╔╝███████╗██║██║ ╚████║██║  ██╗");
//        Bukkit.getConsoleSender().sendMessage("§b╚══════╝ ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝");
//        Bukkit.getConsoleSender().sendMessage("§5==============================================================");

    }

    private void printAmountOfFrequencies() {

        List<String> ingredientList = Echolink.plugin.getConfig().getStringList("echolink_item.shapeless_recipe");
        if(ingredientList.size() > 9 || ingredientList.isEmpty()){
            return;
        }

        Map<String, Integer> countMap = new HashMap<>();
        for(String ingredient : ingredientList){
            countMap.put(ingredient, countMap.getOrDefault(ingredient, 0) + 1);
        }
        if (ingredientList.size() < 9) {
            countMap.put("AIR", 9-ingredientList.size());
        }
        int denominator = 1;
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            denominator *= Utils.factorial(entry.getValue());
        }
        int recipe_amount = Utils.factorial(9) / denominator;
        Utils.tellConsole("# of recipe permutations: "+String.format(Locale.ENGLISH, "%,d", recipe_amount));

        int unicode_amount = UnicodeHandler.UNI_CHARS.size()*(ChatColor.values().length-1);
        Utils.tellConsole("# of unique symbol options: "+String.format(Locale.ENGLISH, "%,d", unicode_amount));

        int frequency_amount = Math.min(recipe_amount, unicode_amount);
        Utils.tellConsole("-> Total # of frequencies = "+String.format(Locale.ENGLISH, "%,d", frequency_amount));

    }

}
