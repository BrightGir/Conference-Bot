package ru.bright.SpringConferenceBot.service.requests;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class WaitingLeaveSeminarIdRequest extends BaseRequest{

    public WaitingLeaveSeminarIdRequest(TelegramBot bot, User user) {
        super(bot, user);
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Введите ID семинара, который вы хотите покинуть");
    }

    @Override
    public boolean handle(Update update) {
        long id;
        try {
            id = Long.parseLong(update.getMessage().getText());
        } catch (NumberFormatException ex) {
            getBot().sendMessage(getUser().getChatId(), "Введите корректный ID");
            return false;
        }
        if(!existSeminarWithId(id)) {
            getBot().sendMessage(getUser().getChatId(), "Семинара с таким ID не существует");
            return false;
        }
        ScienceSeminar seminar = getBot().getSeminarsManager().getSeminarByPrimaryId(id);
        if(!seminar.getParticipants().contains(getUser())) {
            getBot().sendMessage(getUser().getChatId(), "Вы не участвуете в этом семинаре");
            return false;
        }
        getBot().getSeminarsManager().unjoinFrom(getUser(),seminar);
        getBot().sendMessage(getUser().getChatId(), "Вы успешно покинули семинар *" + seminar.getName() + "*","Markdown");
        return true;
    }
    private boolean existSeminarWithId(long id) {
        return getBot().getSeminarsManager().getSeminarByPrimaryId(id) != null;
    }
}
