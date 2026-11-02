package com.evilduck.duckembedder.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class StoppedGuild {

    @Id
    private String guildId;
}
