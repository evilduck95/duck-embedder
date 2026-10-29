package com.evilduck.duckembedder.slashcommand;

import com.evilduck.duckembedder.repository.StoppedGuildsRepository;
import com.evilduck.duckembedder.util.SlashCommandNames;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Resume extends ListenerAdapter implements SlashCommand {

    private final StoppedGuildsRepository stoppedGuildsRepository;

    public Resume(StoppedGuildsRepository stoppedGuildsRepository) {
        this.stoppedGuildsRepository = stoppedGuildsRepository;
    }

    @Override
    public void init(JDA jda) {
        log.info("Initializing resume command");
        jda.upsertCommand(
                Commands.slash(SlashCommandNames.RESUME.commandName(), "Resumes the bot.")
        ).queue();
        jda.addEventListener(this);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals(SlashCommandNames.RESUME.commandName())) {
            if (stoppedGuildsRepository.existsById(event.getGuild().getId())) {
                event.deferReply().queue();
                stoppedGuildsRepository.deleteById(event.getGuild().getId());
                log.info("Resumed guild: " + event.getGuild().getId());
                event.getHook().sendMessage("The bot has been resumed.").queue();
            }
        }
    }
}