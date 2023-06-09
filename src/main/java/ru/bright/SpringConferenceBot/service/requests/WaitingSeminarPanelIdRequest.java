package ru.bright.SpringConferenceBot.service.requests;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.keyboards.InlineSeminarPanelKeyboard;
import ru.bright.SpringConferenceBot.service.requests.editing.WaitingActionRequest;
import ru.bright.SpringConferenceBot.service.utils.Role;

@Slf4j
public class WaitingSeminarPanelIdRequest extends BaseRequest{
    public WaitingSeminarPanelIdRequest(TelegramBot bot, User user) {
        super(bot, user);
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Введите ID семинара, панель которого вы хотите посмотреть");
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
        String s = "Семинар *" + seminar.getName() + "*. ";
        getBot().sendMessage(getUser().getChatId(),s + "Выберите действие:",new InlineSeminarPanelKeyboard(seminar.getId()),"Markdown");
        return true;
    }

    private boolean existSeminarWithId(long id) {
        return getBot().getSeminarsManager().getSeminarByPrimaryId(id) != null;
    }
}
