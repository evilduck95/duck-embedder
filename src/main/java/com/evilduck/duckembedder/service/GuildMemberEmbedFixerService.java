package com.evilduck.duckembedder.service;

import com.evilduck.duckembedder.model.ProxyMapping;
import com.evilduck.duckembedder.repository.ProxyMappingRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class GuildMemberEmbedFixerService {

    private static final Duration DEFAULT_EMBED_TIMEOUT = Duration.ofSeconds(5);
    private static final Set<EmbedType> VALID_EMBED_TYPES = Set.of(
            EmbedType.IMAGE,
            EmbedType.VIDEO
    );

    private final ProxyMappingRepository proxyMappingRepository;

    public GuildMemberEmbedFixerService(final ProxyMappingRepository proxyMappingRepository) {
        this.proxyMappingRepository = proxyMappingRepository;
    }

    public void handleMessage(final Message message) {
        final String messageContentRaw = message.getContentRaw();
        final List<ProxyMapping> proxyMappings = proxyMappingRepository.findAll();
        final Optional<ProxyMapping> applicableMapping = proxyMappings.stream()
                .filter(mapping -> messageContentRaw.contains(mapping.getWebsiteName()))
                .findFirst();
        applicableMapping.ifPresent(mapping -> {
            final Duration embedTimeout = Optional.ofNullable(mapping.getMaxEmbedTime())
                    .orElse(DEFAULT_EMBED_TIMEOUT);
            if (!waitForValidMessageEmbed(message, mapping)) {
                replyWithEmbed(message, mapping, embedTimeout);
            } else {
                log.info("Original message embed is valid");
            }
            log.info("Complete!");
        });
    }

    private boolean waitForValidMessageEmbed(final Message message, final ProxyMapping proxyMapping) {
        log.info("Waiting for message to embed...");
        try {
            final Duration embedTimeout = Optional.ofNullable(proxyMapping.getMaxEmbedTime())
                    .orElse(DEFAULT_EMBED_TIMEOUT);
            TimeUnit.SECONDS.sleep(embedTimeout.getSeconds());
        } catch (InterruptedException e) {
            log.error("Something went wrong waiting for original post to embed: {}", message.getContentRaw(), e);
            throw new RuntimeException(e);
        }
        if (proxyMapping.getWebsiteName().matches(".*twitter.com.*|.*x.com.*")) {
            return messageHasValidTwitterEmbed(message);
        } else {
            return messageHasValidEmbed(message);
        }
    }

    private void replyWithEmbed(final Message originalPost,
                                final ProxyMapping proxyMapping,
                                final Duration embedTimeout) {
        final Set<ProxyMapping.Proxy> proxiesToTry = proxyMapping.getProxyWebsiteNames();
        for (final ProxyMapping.Proxy proxy : proxiesToTry) {
            if (proxy.isActive()) {
                log.info("Trying proxy: {}...", proxy.getName());
                final String replacedMessage = originalPost.getContentRaw().replaceFirst(proxyMapping.getWebsiteName(), proxy.getName());
                final Message reply = originalPost.reply(replacedMessage).mentionRepliedUser(false).complete();
                try {
                    TimeUnit.SECONDS.sleep(embedTimeout.getSeconds());
                } catch (InterruptedException e) {
                    log.error("Something went wrong sleeping replying to message: {}", originalPost.getContentRaw(), e);
                    throw new RuntimeException(e);
                }
                if (messageHasValidEmbed(reply)) {
                    return;
                } else {
                    reply.delete().queue();
                }
            }
        }
        log.info("All proxies failed D:");
    }

    private boolean messageHasValidEmbed(final Message reply) {
        log.info("Checking generic message embeds...");
        final List<MessageEmbed> embeds = reply.getEmbeds();
        log.info("Message embeds: {}", embeds);
        return embeds.stream().anyMatch(this::embedIsValid);
    }

    private boolean messageHasValidTwitterEmbed(final Message reply) {
        log.info("Checking twitter message embeds...");
        final List<MessageEmbed> embeds = reply.getEmbeds();
        return embeds.stream().anyMatch(this::isValidTwitterEmbed);
    }

    private boolean embedIsValid(final MessageEmbed embed) {
        log.info("Embed type is: {} and video info is: {}", embed.getType(), embed.getVideoInfo());
        return VALID_EMBED_TYPES.contains(embed.getType()) ||
                Objects.nonNull(embed.getImage()) ||
                Objects.nonNull(embed.getVideoInfo());
    }

    private boolean isValidTwitterEmbed(final MessageEmbed embed) {
        final boolean isPreviewImage = Optional.ofNullable(embed.getImage())
                .map(MessageEmbed.ImageInfo::getProxyUrl)
                .map(url -> url.contains("media-preview"))
                .orElse(false);
        return !isPreviewImage || Objects.nonNull(embed.getVideoInfo());
    }


}
