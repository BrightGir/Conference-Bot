package ru.bright.bot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.requests.WaitingFIORequest;

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
