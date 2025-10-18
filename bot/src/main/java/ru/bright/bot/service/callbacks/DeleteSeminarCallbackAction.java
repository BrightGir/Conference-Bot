package ru.bright.bot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.requests.WaitingConfirmSeminarDeleteRequest;

public class DeleteSeminarCallbackAction implements CallbackAction{

    private TelegramBot bot;

    public DeleteSeminarCallbackAction(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void action(Update update, User user) {
        String[] m = update.getCallbackQuery().getData().split("_");
        long id = Long.parseLong(m[m.length-1]);
        bot.sendRequest(new WaitingConfirmSeminarDeleteRequest(bot.getSeminarsManager().findById(id),bot,user),5);
    }



}
