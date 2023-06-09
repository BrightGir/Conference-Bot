package ru.bright.SpringConferenceBot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.requests.WaitingConfirmSeminarDeleteRequest;
import ru.bright.SpringConferenceBot.service.requests.editing.WaitingNewSeminarTimeRequest;

public class DeleteSeminarCallbackAction implements CallbackAction{

    private TelegramBot bot;

    public DeleteSeminarCallbackAction(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void action(Update update, User user) {
        String[] m = update.getCallbackQuery().getData().split("_");
        long id = Long.parseLong(m[m.length-1]);
        bot.sendRequest(new WaitingConfirmSeminarDeleteRequest(bot.getSeminarsManager().getSeminarByPrimaryId(id),bot,user),5);
    }



}
