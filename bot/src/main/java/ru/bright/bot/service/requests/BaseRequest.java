package ru.bright.bot.service.requests;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;

public abstract class BaseRequest {

    @Getter
    private TelegramBot bot;

    @Getter
    private User user;

    @Getter
    @Setter
    private boolean cancelled;



    public BaseRequest(TelegramBot bot, User user) {
        this.bot = bot;
        this.user = user;
    }

    public abstract void sendCreateHandleMessage();

    public abstract boolean handle(Update update);


}
