package com.evilduck.duckembedder.configuration;

import com.evilduck.duckembedder.configuration.properties.JdaConfigProps;
import com.evilduck.duckembedder.messaging.GuildMemberMessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

@Configuration
public class JdaConfiguration {

    private final JdaConfigProps jdaConfigProps;
    private final GuildMemberMessageListener guildMemberMessageListener;

    public JdaConfiguration(final JdaConfigProps jdaConfigProps,
                            final GuildMemberMessageListener guildMemberMessageListener) {
        this.jdaConfigProps = jdaConfigProps;
        this.guildMemberMessageListener = guildMemberMessageListener;
    }

    @Bean
    public JDA jda() {
        return JDABuilder.create(
                jdaConfigProps.getToken(),
                EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
        ).addEventListeners(
                guildMemberMessageListener
        ).build();
    }

}
