package ru.bright.SpringConferenceBot.service.requests.editing;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.ApplicationContextProvider;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.keyboards.InlineEditActionsKeyboard;
import ru.bright.SpringConferenceBot.service.requests.BaseRequest;

public class WaitingActionRequest extends BaseRequest {

    private ScienceSeminar seminar;
    public WaitingActionRequest(TelegramBot bot, User user, ScienceSeminar seminar) {
        super(bot, user);
        this.seminar = seminar;
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(),"Выберите действие:",new InlineEditActionsKeyboard(seminar.getId()));
    }

    @Override
    public boolean handle(Update update) {
        return true;
    }
}
