package ru.bright.SpringConferenceBot.service.requests;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;

import java.util.Timer;

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
