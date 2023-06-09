package ru.bright.SpringConferenceBot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.ApplicationContextProvider;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.requests.WaitingFIORequest;
import ru.bright.SpringConferenceBot.service.seminars.SeminarsPagesStore;

public class SetFIOCallbackAction implements CallbackAction{

    private TelegramBot bot;

    public SetFIOCallbackAction(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void action(Update update, User user) {
        bot.sendRequest(new WaitingFIORequest(bot,user),5);
    }


}
