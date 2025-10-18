package ru.bright.bot.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.requests.WaitingSeminarIdToJoinRequest;
import ru.bright.bot.service.utils.Constants;

import java.util.Arrays;
import java.util.List;

public class JoinToSeminarCommand implements Command{

    private TelegramBot bot;

    public JoinToSeminarCommand(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean execute(Update update) {

        User user = bot.getUserManager().getUser(update.getMessage().getChatId());
        if(user.getFio() == null) {
            bot.sendMessage(user.getChatId(),"Чтобы иметь возможность присоединяться к семинарам, заполните профиль");
        } else {
            bot.sendRequest(new WaitingSeminarIdToJoinRequest(bot, user), 5);
        }
        return true;
    }

    @Override
    public List<String> getLabels() {
        return Arrays.asList(Constants.JOIN_TO_SEMINAR_COMMAND);
    }
}
