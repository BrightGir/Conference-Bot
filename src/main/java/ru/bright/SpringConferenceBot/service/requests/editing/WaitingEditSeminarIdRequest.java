package ru.bright.SpringConferenceBot.service.requests.editing;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.requests.BaseRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WaitingEditSeminarIdRequest extends BaseRequest {

    public WaitingEditSeminarIdRequest(TelegramBot bot, User user) {
        super(bot, user);
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Введите ID семинара, которого вы хотите изменить");
    }

    @Override
    public boolean handle(Update update) {
        String msg = update.getMessage().getText();
        Long id;
        try {
            id = Long.parseLong(msg);
        } catch (NumberFormatException e) {
            getBot().sendMessage(update.getMessage().getChatId(),"Введите корректный ID");
            return false;
        }
        ScienceSeminar seminar = getBot().getSeminarsManager().getSeminarByPrimaryId(id);
        if(seminar == null) {
            getBot().sendMessage(update.getMessage().getChatId(),"Вы не являетесь руководителем семинара с таким ID");
            return false;
        }
        getBot().sendRequest(new WaitingActionRequest(getBot(),getUser(),seminar),5);
        return true;
    }

}
