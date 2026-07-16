package com.evilduck.duckembedder.messaging;

import com.evilduck.duckembedder.service.ProxyManagementService;
import com.evilduck.duckembedder.util.SlashCommandNames;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

@Slf4j
public class GuildSlashCommandListener extends ListenerAdapter {

    private final ProxyManagementService proxyManagementService;

    public GuildSlashCommandListener(final ProxyManagementService proxyManagementService) {
        this.proxyManagementService = proxyManagementService;
    }

    @Override
    public void onSlashCommandInteraction(final SlashCommandInteractionEvent event) {
        final String commandName = event.getName();
        if (commandName.equals(SlashCommandNames.DISABLE_PROXY.commandName())) {
            final OptionMapping website = event.getOption("website");
            if (website == null) {
                throw new NullPointerException("Option \"website\" was null for slash event: " + event.getName());
            }
            final boolean deleted = proxyManagementService.disableProxy(website.getAsString());
            if (deleted) {
                event.reply( website.getAsString() + " has been disabled").queue();
            } else {
                event.reply("Unable to find: " + website.getAsString()).queue();
            }
        } else {
            event.reply("This command is not yet implemented, please try again later").queue();
            log.error("Slash command invoked without implementation, {}", commandName);
            throw new UnsupportedOperationException("Slash command listener was invoked without an implementation");
        }
    }
}
