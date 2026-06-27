package com.evilduck.duckembedder.messaging;

import com.evilduck.duckembedder.service.GuildMemberEmbedFixerService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Slf4j
public class GuildMemberMessageListener extends ListenerAdapter {

    private final GuildMemberEmbedFixerService guildMemberEmbedFixerService;

    public GuildMemberMessageListener(final GuildMemberEmbedFixerService guildMemberEmbedFixerService) {
        this.guildMemberEmbedFixerService = guildMemberEmbedFixerService;
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            log.info("Received message: {}", event.getMessage().getContentRaw());
            guildMemberEmbedFixerService.handleMessage(event.getMessage());
        }
    }

}
