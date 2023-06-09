package ru.bright.SpringConferenceBot.service.requests.seminar;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.requests.BaseRequest;

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
        ScienceSeminar seminar = new ScienceSeminar();
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
