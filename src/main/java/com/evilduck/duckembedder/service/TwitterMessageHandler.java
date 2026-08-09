package com.evilduck.duckembedder.service;

import com.evilduck.duckembedder.model.ProxyMapping;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Component
public class TwitterMessageHandler implements WebsiteEmbedHandler {

    private static final Pattern WEBSITE_PATTERN = Pattern.compile("https://(twitter|x).com.*");

    @Override
    public boolean isApplicable(final ProxyMapping proxyMapping) {
        return WEBSITE_PATTERN.matcher(proxyMapping.getWebsiteName()).matches();
    }

    @Override
    public boolean hasValidEmbed(final Message message) {
        log.info("Checking embeds for message content [{}]...", message.getContentRaw());
        final List<MessageEmbed> embeds = message.getEmbeds();
        return embeds.stream().anyMatch(this::isValidTwitterEmbed);
    }

    private boolean isValidTwitterEmbed(final MessageEmbed embed) {
        final boolean isPreviewImage = Optional.ofNullable(embed.getImage())
                .map(MessageEmbed.ImageInfo::getProxyUrl)
                .map(url -> url.contains("media-preview"))
                .orElse(false);
        final boolean hasVideoInfo = Objects.nonNull(embed.getVideoInfo());
        if (isPreviewImage) log.info("Message has no image or image is not media preview");
        if (hasVideoInfo) log.info("Message has video info");
        return !isPreviewImage || hasVideoInfo;
    }

}
