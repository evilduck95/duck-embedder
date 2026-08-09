package com.evilduck.duckembedder.service;

import com.evilduck.duckembedder.model.ProxyMapping;
import net.dv8tion.jda.api.entities.Message;

public interface WebsiteEmbedHandler {

    boolean isApplicable(final ProxyMapping proxyMapping);

    boolean hasValidEmbed(final Message message);

}
