package ru.bright.SpringConferenceBot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.requests.editing.WaitingEditSeminarIdRequest;
import ru.bright.SpringConferenceBot.service.requests.editing.WaitingNewSeminarTimeRequest;

public class EditSeminarCallbackAction implements CallbackAction{

    private TelegramBot bot;

    public EditSeminarCallbackAction(TelegramBot bot) {
        this.bot = bot;
    }
    @Override
    public void action(Update update, User user) {
        bot.sendRequest(new WaitingEditSeminarIdRequest(bot,user),5);
    }


}
