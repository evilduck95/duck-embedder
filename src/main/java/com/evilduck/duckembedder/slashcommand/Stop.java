package com.evilduck.duckembedder.slashcommand;

import com.evilduck.duckembedder.model.StoppedGuild;
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
public class Stop extends ListenerAdapter implements SlashCommand{

    private final StoppedGuildsRepository stoppedGuildsRepository;

    public Stop(StoppedGuildsRepository stoppedGuildsRepository) {
        this.stoppedGuildsRepository = stoppedGuildsRepository;
    }

    @Override
    public void init(final JDA jda) {
        log.info("Initialising Stop slash command");
        jda.upsertCommand(
                Commands.slash(SlashCommandNames.STOP.commandName(), "Stops the bot.")
        ).queue();
        jda.addEventListener(this);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        //On second thought we might not even need the conditional at all if the repository doesn't allow duplicates.
        if(event.getName().equals(SlashCommandNames.STOP.commandName())) {
            if (!stoppedGuildsRepository.existsById(event.getGuild().getId())) {
                event.deferReply().queue();
                StoppedGuild guild = new StoppedGuild(event.getGuild().getId());
                stoppedGuildsRepository.save(guild);
                log.info("Stopped guild: " + event.getGuild().getId());
                event.getHook().sendMessage("The bot has been stopped.").queue();
            }
        }
    }
}