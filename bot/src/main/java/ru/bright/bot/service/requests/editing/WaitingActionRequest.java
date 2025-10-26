package ru.bright.bot.service.requests.editing;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.keyboards.InlineEditActionsKeyboard;
import ru.bright.bot.service.requests.BaseRequest;

public class WaitingActionRequest extends BaseRequest {

    private SeminarDTO seminar;
    public WaitingActionRequest(TelegramBot bot, User user, SeminarDTO seminar) {
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
