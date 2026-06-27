package com.evilduck.duckembedder.configuration;

import com.evilduck.duckembedder.messaging.GuildMemberMessageListener;
import com.evilduck.duckembedder.service.GuildMemberEmbedFixerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageListenerConfiguration {

    @Bean
    public GuildMemberMessageListener guildMessageListener(final GuildMemberEmbedFixerService guildMemberEmbedFixerService) {
        return new GuildMemberMessageListener(guildMemberEmbedFixerService);
    }

}
