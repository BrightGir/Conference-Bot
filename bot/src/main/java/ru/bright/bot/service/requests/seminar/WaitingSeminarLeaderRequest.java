package ru.bright.bot.service.requests.seminar;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.requests.BaseRequest;

public class WaitingSeminarLeaderRequest extends BaseRequest {
    public WaitingSeminarLeaderRequest(TelegramBot bot, User user) {
        super(bot, user);
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Введите руководителя семинара (ФИО)");
    }

    @Override
    public boolean handle(Update update) {
        SeminarDTO seminar = new SeminarDTO();
        String FIO = update.getMessage().getText();
        if(FIO.length() > 60) {
            getBot().sendMessage(update.getMessage().getChatId(),"Максимальное количество символов: 60");
            return false;
        }
        seminar.setChatIdOwner(update.getMessage().getChatId());
        seminar.setLeaderFIO(FIO);
        getBot().sendRequest(new WaitingSeminarNameRequest(getBot(),getUser(),seminar),5);
        return false;
    }
}
