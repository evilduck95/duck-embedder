package com.evilduck.duckembedder.configuration;

import com.evilduck.duckembedder.configuration.properties.JdaConfigProps;
import com.evilduck.duckembedder.messaging.GuildMemberMessageListener;
import com.evilduck.duckembedder.slashcommand.SlashCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.List;

@Configuration
public class JdaConfiguration {

    private final JdaConfigProps jdaConfigProps;
    private final List<SlashCommand> slashCommands;
    private final GuildMemberMessageListener guildMemberMessageListener;

    public JdaConfiguration(final JdaConfigProps jdaConfigProps,
                            final List<SlashCommand> slashCommands,
                            final GuildMemberMessageListener guildMemberMessageListener) {
        this.jdaConfigProps = jdaConfigProps;
        this.slashCommands = slashCommands;
        this.guildMemberMessageListener = guildMemberMessageListener;
    }

    @Bean
    public JDA jda() throws InterruptedException {
        final JDA builtJda = JDABuilder.create(
                jdaConfigProps.getToken(),
                EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
        ).addEventListeners(
                guildMemberMessageListener
        ).build().awaitReady();
        for (final SlashCommand slashCommand : slashCommands) {
            slashCommand.init(builtJda);
        }
        return builtJda;
    }

}
