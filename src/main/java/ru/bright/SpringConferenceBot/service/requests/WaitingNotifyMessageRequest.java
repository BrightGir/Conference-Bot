package ru.bright.SpringConferenceBot.service.requests;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;

public class WaitingNotifyMessageRequest extends BaseRequest{

    private ScienceSeminar seminar;
    public WaitingNotifyMessageRequest(TelegramBot bot, User user, ScienceSeminar seminar) {
        super(bot, user);
        this.seminar = seminar;
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(),"Введите уведомление для всех участников");
    }

    @Override
    public boolean handle(Update update) {
        String notify = update.getMessage().getText();
        if(notify.length() > 100) {
            getBot().sendMessage(getUser().getChatId(),"Максимальное количество символов: 100");
            return false;
        }
        getBot().getSeminarsManager().notify(getBot(),seminar,notify);
        getBot().sendMessage(getUser().getChatId(),"Уведомление отправлено!");
        return true;
    }
}
