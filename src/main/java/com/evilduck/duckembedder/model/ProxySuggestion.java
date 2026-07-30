package com.evilduck.duckembedder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Data
@AllArgsConstructor
public class ProxySuggestion {

    @Id
    private String websiteName;
    private Map<String, Integer> proxyVotes;

}
