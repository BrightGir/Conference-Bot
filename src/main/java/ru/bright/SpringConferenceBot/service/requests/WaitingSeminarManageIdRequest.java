package ru.bright.SpringConferenceBot.service.requests;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.keyboards.InlineEditActionsKeyboard;
import ru.bright.SpringConferenceBot.service.requests.editing.WaitingActionRequest;
import ru.bright.SpringConferenceBot.service.utils.Role;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class WaitingSeminarManageIdRequest extends BaseRequest {
    public WaitingSeminarManageIdRequest(TelegramBot bot, User user) {
        super(bot, user);
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(),"Введите ID семинара, которым вы хотите управлять");
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
        if(seminar.getChatIdOwner().longValue() != getUser().getChatId() && !getUser().getRole().equals(Role.ADMIN.toString())) {
            getBot().sendMessage(getUser().getChatId(), "Вы не являетесь руководителем семинара с таким ID");
            return false;
        }
        String s = "Семинар *" + seminar.getName() + "*. ";
        getBot().sendMessage(getUser().getChatId(),s + "Выберите действие:",new InlineEditActionsKeyboard(seminar.getId()),"Markdown");
        return true;
    }

    private boolean existSeminarWithId(long id) {
        return getBot().getSeminarsManager().getSeminarByPrimaryId(id) != null;
    }
}
