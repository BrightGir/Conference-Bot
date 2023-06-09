package ru.bright.SpringConferenceBot.service.requests.seminar;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.requests.BaseRequest;

public class WaitingSeminarNameRequest extends BaseRequest {

    private ScienceSeminar seminar;
    public WaitingSeminarNameRequest(TelegramBot bot, User user, ScienceSeminar seminar) {
        super(bot, user);
        this.seminar = seminar;
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Введите название семинара");
    }

    @Override
    public boolean handle(Update update) {
        String name = update.getMessage().getText();
        if(name.length() > 150) {
            getBot().sendMessage(update.getMessage().getChatId(),"Максимальное количество символов: 150");
            return false;
        }
        seminar.setName(name);
        getBot().sendRequest(new WaitingExtraSeminarInfoRequest(getBot(),getUser(),seminar),5);
        return false;
    }
}
