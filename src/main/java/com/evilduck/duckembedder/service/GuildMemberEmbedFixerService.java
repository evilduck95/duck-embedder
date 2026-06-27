package com.evilduck.duckembedder.service;

import com.evilduck.duckembedder.model.ProxyMapping;
import com.evilduck.duckembedder.repository.ProxyMappingRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class GuildMemberEmbedFixerService {

    private static final Set<EmbedType> VALID_EMBED_TYPES = Set.of(
            EmbedType.RICH,
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
        if (applicableMapping.isPresent()) {
            final ProxyMapping mapping = applicableMapping.get();
//            return mapping.getProxyWebsiteNames().stream()
//                    .filter(ProxyMapping.Proxy::isActive)
//                    .findFirst()
//                    .map(proxy -> {
//                        sessionCache.addSessionProxy(message.getId(), proxy);
//                        return messageContentRaw.replaceFirst(mapping.getWebsiteName(), proxy.getName());
//                    });
            replyWithEmbed(message, mapping);
        }
    }

    private void replyWithEmbed(final Message originalPost,
                                final ProxyMapping proxyMapping) {
        final Set<ProxyMapping.Proxy> proxiesToTry = proxyMapping.getProxyWebsiteNames();
        for (final ProxyMapping.Proxy proxy : proxiesToTry) {
            if (proxy.isActive()) {
                final String replacedMessage = originalPost.getContentRaw().replaceFirst(proxyMapping.getWebsiteName(), proxy.getName());
                final Message reply = originalPost.reply(replacedMessage).mentionRepliedUser(false).complete();
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    log.error("Something went wrong sleeping replying to message: {}", originalPost.getContentRaw(), e);
                    throw new RuntimeException(e);
                }
                if (replyIsValid(reply)) {
                    return;
                } else {
                    reply.delete().queue();
                }
            }
        }
    }

    private boolean replyIsValid(final Message reply) {
        final List<MessageEmbed> embeds = reply.getEmbeds();
        return embeds.stream().anyMatch(this::embedIsValid);
    }

    private boolean embedIsValid(final MessageEmbed embed) {
        return VALID_EMBED_TYPES.contains(embed.getType()) ||
                Objects.nonNull(embed.getImage()) ||
                Objects.nonNull(embed.getVideoInfo());
    }


}
