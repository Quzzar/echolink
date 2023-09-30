package com.quzzar.echolink;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Utils {

    public static final ItemStack echolinkFrame = getEcholinkItemFrame();

    public static boolean isFairlySimilar(ItemStack item1, ItemStack item2) {

        // If they have lore, clear it
        if(item1.hasItemMeta() && item1.getItemMeta().hasLore()) {
            item1 = item1.clone();
            ItemMeta im = item1.getItemMeta();
            im.setLore(null);
            item1.setItemMeta(im);
        }

        if(item2.hasItemMeta() && item2.getItemMeta().hasLore()) {
            item2 = item2.clone();
            ItemMeta im = item2.getItemMeta();
            im.setLore(null);
            item2.setItemMeta(im);
        }

        return item1.isSimilar(item2);
    }


    public static void tellSender(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.LIGHT_PURPLE+"["+ChatColor.AQUA+"Echolink"+ChatColor.LIGHT_PURPLE+"] "+ChatColor.RESET+message);
    }

    public static void tellConsole(String message){
        Utils.tellSender(Bukkit.getConsoleSender(), message);
    }

    public static void needsPermission(CommandSender sender) {
        sender.sendMessage("§cYou don't have permission to use this command!");
    }

    public static void sendPlayerMessage(String channelPrefix, Player sender, Player receiver, String message) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(" ");

        if(!channelPrefix.isEmpty()) {
            strBuilder.append(channelPrefix);
            strBuilder.append("§r§7 > §r");
        }

        String rankPrefix = Echolink.rankPrefixManager.getRankPrefix(sender);
        if(!rankPrefix.isEmpty()) {
            strBuilder.append(rankPrefix);
            strBuilder.append(" §r");
        }

        strBuilder.append(sender.getDisplayName());
        strBuilder.append(" §7-§r ");
        strBuilder.append(message);

        receiver.sendMessage(strBuilder.toString());

    }

    public static int factorial(int number) {
        int result = 1;
        for (int factor = 2; factor <= number; factor++) {
            result = result * factor;
        }
        return result;
    }

    public static List<ItemStack> getPlayerEcholinks(Player p) {

        List<ItemStack> items = new ArrayList<>();

        ItemStack helmet = p.getInventory().getHelmet();
        if(helmet != null) {
            helmet = helmet.clone();
            helmet.setAmount(1);
            if (isFairlySimilar(helmet, echolinkFrame)) {
                items.add(helmet);
            }
        }

        ItemStack offhand = p.getInventory().getItemInOffHand().clone();
        offhand.setAmount(1);
        if(isFairlySimilar(offhand, echolinkFrame)) {
            items.add(offhand);
        }

        // Iterate through hot bar slots (0 to 8)
        for (int i = 0; i < 9; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null) continue;
            item = item.clone();
            item.setAmount(1);
            if (isFairlySimilar(item, echolinkFrame)) {
                items.add(item);
            }
        }

        return items;
    }

    public static List<Player> getFreqRecipients(List<ItemStack> echolinks) {
        List<Player> recipients = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null) continue;
                item = item.clone();
                item.setAmount(1);
                if (echolinks.contains(item)) {
                    recipients.add(player);
                    break;
                }
            }
        }

        return recipients;
    }

    public static List<ItemStack> getUniqueItems(List<ItemStack> items) {

        List<ItemStack> combinedList = new ArrayList<>();

        for (ItemStack item : items) {
            if (item == null) continue;
            ItemStack itemClone = item.clone();
            itemClone.setAmount(1);

            if(!combinedList.contains(itemClone)){
                combinedList.add(itemClone);
            }
        }

        return combinedList;
    }

    public static ItemStack getEcholinkItemFrame() {
        // Create an ItemStack for a player head
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);

        // Get the SkullMeta of the player head
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();

        // Set the display name
        skullMeta.setDisplayName(ChatColor.WHITE+"Echolink");

        skullMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ITEM_SPECIFICS);

        String textureURL = Echolink.plugin.getConfig().getString("echolink_item.head_block_texture");
        if (textureURL == null) {
            Utils.tellConsole(org.bukkit.ChatColor.RED+"Invalid item texture URL");
            return playerHead;
        }

        byte[] hashedBytes = hashString(textureURL);
        if (hashedBytes == null) {
            Utils.tellConsole(org.bukkit.ChatColor.RED+"Unable to hash item texture URL");
            return playerHead;
        }
        String ownerUUID = createUUIDFromBytes(hashedBytes).toString();
        skullMeta.setOwningPlayer(Bukkit.getServer().getOfflinePlayer(ownerUUID));

        // Set the custom texture (Properties) if needed
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", textureURL).getBytes());

        PlayerProfile profile = skullMeta.getPlayerProfile();
        if (profile == null) {
            Utils.tellConsole(org.bukkit.ChatColor.RED+"Invalid player profile for item texture");
            return playerHead;
        }
        Set<ProfileProperty> properties = profile.getProperties();
        properties.add(new ProfileProperty("textures", new String(encodedData)));
        profile.setProperties(properties);

        // Apply the SkullMeta to the ItemStack
        skullMeta.setPlayerProfile(profile);
        playerHead.setItemMeta(skullMeta);

        return playerHead;
    }


    public static ItemStack getEcholinkItem(ItemStack item, ItemStack[] matrix) {

        ItemMeta im = item.getItemMeta();
        im.setLore(Collections.singletonList("§7§oFreq: §r" + serializeMatrix(matrix)));
        item.setItemMeta(im);

        return item;
    }

    public static String getFreqFromItem(ItemStack echolink) {
        String text = echolink.getItemMeta().getLore().get(0);
        String[] parts = text.split("Freq: ");
        if(parts.length > 1){
            return parts[1];
        }
        return null;
    }

    private static String serializeMatrix(ItemStack[] matrix) {

        StringBuilder strBuilder = new StringBuilder();
        for(ItemStack item : matrix) {
            if(item!=null) {
                strBuilder.append(",,,").append(item.getType().name()).append(",,,");
            } else {
                strBuilder.append("~~~");
            }
        }
        String matrixStr = strBuilder.toString();

        int hashCode = Math.abs(matrixStr.hashCode());
        ChatColor[] colors = ChatColor.values();

        ChatColor color = colors[hashCode % colors.length];
        if(color == ChatColor.MAGIC){ color = ChatColor.WHITE; }

        return color+""+UnicodeHandler.UNI_CHARS.get(hashCode % UnicodeHandler.UNI_CHARS.size());
    }


    private static UUID createUUIDFromBytes(byte[] bytes) {
        // Set the version bits to 4 for UUID version 4 (randomly generated UUID)
        bytes[6] &= 0x0f;
        bytes[6] |= 0x40;

        // Set the variant bits to 2 for Leach-Salz variant
        bytes[8] &= 0x3f;
        bytes[8] |= 0x80;

        // Convert the byte array to a UUID
        long mostSigBits = 0;
        long leastSigBits = 0;
        for (int i = 0; i < 8; i++) {
            mostSigBits = (mostSigBits << 8) | (bytes[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            leastSigBits = (leastSigBits << 8) | (bytes[i] & 0xff);
        }

        return new UUID(mostSigBits, leastSigBits);
    }

    private static byte[] hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
