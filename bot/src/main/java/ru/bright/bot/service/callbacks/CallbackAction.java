package ru.bright.bot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;

public interface CallbackAction {

    void action(Update update, User user);

    //List<String> getCallbackData();
}
