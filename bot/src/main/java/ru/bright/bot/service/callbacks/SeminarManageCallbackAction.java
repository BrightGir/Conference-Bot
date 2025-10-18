package ru.bright.bot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.requests.WaitingSeminarManageIdRequest;

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
