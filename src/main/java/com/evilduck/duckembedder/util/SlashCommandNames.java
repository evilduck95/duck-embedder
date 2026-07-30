package com.evilduck.duckembedder.util;

public enum SlashCommandNames {

    SUGGEST("suggest");

    final String commandName;

    SlashCommandNames(final String commandName) {
        this.commandName = commandName;
    }

    public String commandName() {
        return commandName;
    }

}
