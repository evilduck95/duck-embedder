package com.evilduck.duckembedder.slashcommand;

import com.evilduck.duckembedder.model.ProxySuggestion;
import com.evilduck.duckembedder.repository.ProxySuggestionRepository;
import com.evilduck.duckembedder.util.SlashCommandNames;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class Suggestion extends ListenerAdapter implements SlashCommand {

    private static final String WEBSITE_NAME = "website";
    private static final String SUGGESTION = "suggestion";
    private static final String HTTPS_PROTOCOL = "https://";

    private final ProxySuggestionRepository proxySuggestionRepository;

    public Suggestion(final ProxySuggestionRepository proxySuggestionRepository) {
        this.proxySuggestionRepository = proxySuggestionRepository;
    }

    @Override
    public void init(final JDA jda) {
        log.info("Initialising Suggestion slash command");
        jda.upsertCommand(
                        Commands.slash(SlashCommandNames.SUGGEST.commandName(), "Adds a suggested proxy for a popular website for better embedding")
                                .addOption(OptionType.STRING, WEBSITE_NAME, "The website that won't embed properly", true)
                                .addOption(OptionType.STRING, SUGGESTION, "The website that would properly embed your links", true))
                .queue();
        jda.addEventListener(this);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
        if (event.getName().equals(SlashCommandNames.SUGGEST.commandName())) {
            event.deferReply().queue();
            final OptionMapping websiteNameOption = event.getOption(WEBSITE_NAME);
            final OptionMapping suggestedProxyOption = event.getOption(SUGGESTION);
            // Both options need to be set to process
            if (Objects.nonNull(websiteNameOption) && Objects.nonNull(suggestedProxyOption)) {
                final ProxySuggestion proxySuggestion = findProxySuggestionForWebsite(websiteNameOption);

                // Get the root of the website (we don't want /any/paths/anywhere)
                final String suggestedProxy = getAsRootUrl(suggestedProxyOption.getAsString());
                // Increment the votes for this suggestion (or start a new vote for it)
                final Map<String, Integer> updatedVotes = incrementVotesForProxy(proxySuggestion, suggestedProxy);
                proxySuggestion.setProxyVotes(updatedVotes);
                proxySuggestionRepository.save(proxySuggestion);

                // Let the user know we processed
                event.getHook().sendMessage("Saved %s proxy suggestion for %s".formatted(suggestedProxy, proxySuggestion.getWebsiteName()))
                        .setSuppressEmbeds(true)
                        .setEphemeral(true)
                        .queue();
            } else {
                event.getHook().sendMessage("Missing website name or suggested proxy")
                        .setEphemeral(true)
                        .queue();
            }
        }
    }

    // Get the current vote (or 0) and increment the map entry. Return a copy of the map.
    private Map<String, Integer> incrementVotesForProxy(final ProxySuggestion proxySuggestion,
                                                        final String suggestedProxy) {
        final Map<String, Integer> voteMap = new HashMap<>(proxySuggestion.getProxyVotes());
        final Integer votesForProxy = voteMap.getOrDefault(suggestedProxy, 0);
        voteMap.put(suggestedProxy, votesForProxy + 1);
        return voteMap;
    }

    // Find the database entry for the mapping, return a fresh one if there isn't one, so we can always accept.
    private ProxySuggestion findProxySuggestionForWebsite(final OptionMapping optionMapping) {
        final String websiteName = getAsRootUrl(optionMapping.getAsString());
        return proxySuggestionRepository.findById(websiteName)
                .orElse(new ProxySuggestion(websiteName, new HashMap<>()));
    }

    private String getAsRootUrl(final String websiteName) {
        final URI websiteUri = websiteName.startsWith(HTTPS_PROTOCOL) ?
                URI.create(websiteName) :
                URI.create(HTTPS_PROTOCOL + websiteName);
        return websiteUri.resolve("/").toString();
    }
}
