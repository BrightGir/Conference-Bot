package ru.bright.bot.service.requests.seminar;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.requests.BaseRequest;

public class WaitingExtraSeminarInfoRequest extends BaseRequest {

    private ScienceSeminar seminar;


    public WaitingExtraSeminarInfoRequest(TelegramBot bot, User user, ScienceSeminar seminar) {
        super(bot, user);
        this.seminar = seminar;
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Введите дополнительную информацию о семинаре (25 строк максимум)");
    }

    @Override
    public boolean handle(Update update) {
        String info = update.getMessage().getText();
        int str = info.split("\n").length;
        if(str > 25) {
            getBot().sendMessage(update.getMessage().getChatId(),"Максимальное количество строк: 25");
            return false;
        }
        seminar.setAdditionalInformation(info);
        getBot().sendRequest(new WaitingSeminarTimeRequest(getBot(),getUser(),seminar),5);
        return false;
    }
}
