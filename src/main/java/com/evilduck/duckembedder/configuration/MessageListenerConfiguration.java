package com.evilduck.duckembedder.configuration;

import com.evilduck.duckembedder.messaging.GuildMemberMessageListener;
import com.evilduck.duckembedder.messaging.GuildSlashCommandListener;
import com.evilduck.duckembedder.service.GuildMemberEmbedFixerService;
import com.evilduck.duckembedder.service.ProxyManagementService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageListenerConfiguration {

    @Bean
    public GuildMemberMessageListener guildMessageListener(final GuildMemberEmbedFixerService guildMemberEmbedFixerService) {
        return new GuildMemberMessageListener(guildMemberEmbedFixerService);
    }

    @Bean
    public GuildSlashCommandListener guildSlashCommandListener(final ProxyManagementService proxyManagementService) {
        return new GuildSlashCommandListener(proxyManagementService);
    }

}
