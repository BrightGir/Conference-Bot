package ru.bright.bot.service.requests;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.keyboards.InlineSeminarPanelKeyboard;

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
        SeminarDTO seminar = getBot().getSeminarsManager().findById(id);
        if(!seminar.getParticipants().contains(getUser())) {
            getBot().sendMessage(getUser().getChatId(), "Вы не участвуете в этом семинаре");
            return false;
        }
        String s = "Семинар *" + seminar.getName() + "*. ";
        getBot().sendMessage(getUser().getChatId(),s + "Выберите действие:",new InlineSeminarPanelKeyboard(seminar.getId()),"Markdown");
        return true;
    }

    private boolean existSeminarWithId(long id) {
        return getBot().getSeminarsManager().findById(id) != null;
    }
}
