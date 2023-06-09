package ru.bright.SpringConferenceBot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.User;

import java.util.List;

public interface CallbackAction {

    void action(Update update, User user);

    //List<String> getCallbackData();
}
