package com.quzzar.echolink.rankprefix;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.quzzar.echolink.Echolink;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class RankPrefixManager {

    private final ArrayList<RankPrefix> rankPrefixes;

    public RankPrefixManager() {

        rankPrefixes = new ArrayList<RankPrefix>();

        ConfigurationSection section = Echolink.plugin.getConfig().getConfigurationSection("rank_prefixes");
        if(section == null) { return; }

        Set<String> rSet = section.getKeys(false);
        List<String> ranks = new ArrayList<String>(rSet);
        //ranks.sort(Collections.reverseOrder());

        for(String rankName : ranks) {

            String prefix = Echolink.plugin.getConfig().getString("rank_prefixes."+rankName+".prefix");
            rankPrefixes.add(new RankPrefix(rankName, prefix));

        }

    }

    public String getRankPrefix(Player player) {
        for(RankPrefix rankPrefix : rankPrefixes) {
            if(player.hasPermission(rankPrefix.getPermission())) {
                return rankPrefix.getPrefix();
            }
        }
        return "";
    }

}
