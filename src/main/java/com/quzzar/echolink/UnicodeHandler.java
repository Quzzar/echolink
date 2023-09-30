package com.quzzar.echolink;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class UnicodeHandler {

    public static List<Character> UNI_CHARS = new ArrayList<>();
    public static void initUnicodeCharacters() {
        UNI_CHARS = getUnicodeCharacters();
    }

    private static List<Character> getUnicodeCharacters() {

        List<String> blockList = Echolink.plugin.getConfig().getStringList("echolink_item.unicode_symbol_blocks");

        // Define the Unicode code point ranges for the desired blocks
        List<Range> unicodeRanges = new ArrayList<>();
        for(String range : blockList){
            try {
                unicodeRanges.add(Range.parseFromString(range));
            } catch (Exception ignored) {
                Utils.tellConsole(ChatColor.RED+"Invalid unicode range: "+range);
            }
        }

        // Create a list to store the characters
        List<Character> unicodeCharacters = new ArrayList<>();

        // Populate the list with characters from the specified Unicode ranges
        for (Range range : unicodeRanges) {
            for (int codePoint = range.start; codePoint <= range.end; codePoint++) {
                unicodeCharacters.add((char) codePoint);
            }
        }
        return unicodeCharacters;
    }

    static class Range {
        int start;
        int end;

        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public static Range parseFromString(String rangeString) {
            if (rangeString != null && rangeString.matches("0x[0-9A-Fa-f]+-0x[0-9A-Fa-f]+")) {
                String[] parts = rangeString.split("-");
                int start = Integer.parseInt(parts[0].substring(2), 16);
                int end = Integer.parseInt(parts[1].substring(2), 16);
                return new Range(start, end);
            } else {
                throw new IllegalArgumentException("Invalid range format: " + rangeString);
            }
        }
    }

}
