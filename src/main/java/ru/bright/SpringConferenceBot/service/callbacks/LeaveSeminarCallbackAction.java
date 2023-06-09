package ru.bright.SpringConferenceBot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.ApplicationContextProvider;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.requests.WaitingConfirmLeaveSeminarRequest;
import ru.bright.SpringConferenceBot.service.seminars.SeminarsPagesStore;

public class LeaveSeminarCallbackAction implements CallbackAction {

    private TelegramBot bot;


    public LeaveSeminarCallbackAction(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void action(Update update, User user) {
        long id = Long.parseLong(update.getCallbackQuery().getData().split("_")[2]);;
        ScienceSeminar seminar = bot.getSeminarsManager().getSeminarByPrimaryId(id);
        bot.sendRequest(new WaitingConfirmLeaveSeminarRequest(bot,user,seminar),5);
    }

}
