package com.evilduck.duckembedder.init;

import com.evilduck.duckembedder.util.SlashCommandNames;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.stereotype.Component;

@Component
public class JdaInitialisation {

    private final JDA jda;

    public JdaInitialisation(final JDA jda) {
        this.jda = jda;
    }

    @PostConstruct
    public void initSlashCommands() {
        final SlashCommandData disableProxyCommand = Commands.slash(
                        SlashCommandNames.DISABLE_PROXY.commandName(),
                        "Disables a website proxy")
                .addOption(
                        OptionType.STRING,
                        "website",
                        "The website url to disable",
                        true
                );
        jda.upsertCommand(disableProxyCommand).queue();
    }

}
