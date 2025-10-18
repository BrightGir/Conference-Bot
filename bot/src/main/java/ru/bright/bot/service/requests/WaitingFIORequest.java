package ru.bright.bot.service.requests;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;

public class WaitingFIORequest extends BaseRequest{

    public WaitingFIORequest(TelegramBot bot, User user) {
        super(bot, user);
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Введите своё ФИО:");
    }

    @Override
    public boolean handle(Update update) {
        String FIO = update.getMessage().getText();
        try {
            Integer.parseInt(FIO);
            getBot().sendMessage(getUser().getChatId(), "ФИО не может быть числом");
            return false;
        } catch (NumberFormatException ex) {

        }
        getUser().setFio(FIO);
        getBot().getUserManager().saveUser(getUser());
        getBot().sendMessage(getUser().getChatId(), "Вы успешно установили свое ФИО");
        return true;
    }
}
