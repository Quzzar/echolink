package com.quzzar.echolink.localchat;

import com.quzzar.echolink.Echolink;

public class LocalChatManager {

    public static boolean randomForMissing;

    private final ChatProcessor normalProcessor;
    private final ChatProcessor shoutProcessor;
    private final ChatProcessor whisperProcessor;

    public LocalChatManager() {

        randomForMissing = Echolink.plugin.getConfig().getBoolean("local_chat.use_magic_concealment");

        normalProcessor = new ChatProcessor(Echolink.plugin.getConfig().getString("local_chat.normal.prefix"),
                Echolink.plugin.getConfig().getInt("local_chat.normal.base_range"),
                Echolink.plugin.getConfig().getInt("local_chat.normal.extended_conceal_range"));
        whisperProcessor = new ChatProcessor(Echolink.plugin.getConfig().getString("local_chat.whisper.prefix"),
                Echolink.plugin.getConfig().getInt("local_chat.whisper.base_range"),
                Echolink.plugin.getConfig().getInt("local_chat.whisper.extended_conceal_range"));
        shoutProcessor = new ChatProcessor(Echolink.plugin.getConfig().getString("local_chat.shout.prefix"),
                Echolink.plugin.getConfig().getInt("local_chat.shout.base_range"),
                Echolink.plugin.getConfig().getInt("local_chat.shout.extended_conceal_range"));

    }

    public ChatProcessor getShoutProcessor() {
        return shoutProcessor;
    }

    public ChatProcessor getNormalProcessor() {
        return normalProcessor;
    }

    public ChatProcessor getWhisperProcessor() {
        return whisperProcessor;
    }

}
