package ru.bright.bot.service.requests.editing;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.requests.BaseRequest;

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
        ScienceSeminar seminar = getBot().getSeminarsManager().findById(id);
        if(seminar == null) {
            getBot().sendMessage(update.getMessage().getChatId(),"Вы не являетесь руководителем семинара с таким ID");
            return false;
        }
        getBot().sendRequest(new WaitingActionRequest(getBot(),getUser(),seminar),5);
        return true;
    }

}
