package ru.bright.bot.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface Command {

    boolean execute(Update update);

    List<String> getLabels();

}
