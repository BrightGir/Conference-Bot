package ru.bright.bot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.utils.Role;
import ru.bright.bot.service.utils.Token;
import ru.bright.bot.service.utils.TokenGenerator;

@Component
@Slf4j
public class BotInitializer {

    @Autowired
    private TelegramBot telegramBot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        System.out.println("Initializing Bot");
        try {
            telegramBotsApi.registerBot(telegramBot);
            Token token = TokenGenerator.generateToken(Integer.MAX_VALUE, Role.ADMIN);
            telegramBot.registerToken(token);
        } catch (TelegramApiException e) {
            log.error("An error occurred " + e.getMessage());
        }

    }
}
