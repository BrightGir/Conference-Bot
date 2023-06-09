package ru.bright.SpringConferenceBot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.requests.WaitingSeminarManageIdRequest;

public class SeminarManageCallbackAction implements CallbackAction {

    private TelegramBot bot;

    public SeminarManageCallbackAction(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void action(Update update, User user) {
        bot.sendRequest(new WaitingSeminarManageIdRequest(bot,user),5);
    }

}
