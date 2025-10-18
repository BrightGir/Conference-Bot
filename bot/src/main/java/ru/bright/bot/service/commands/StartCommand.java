package ru.bright.bot.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

import ru.bright.bot.model.User;
import ru.bright.bot.service.utils.Constants;
import ru.bright.bot.service.utils.Role;
import ru.bright.bot.service.TelegramBot;

import java.util.Arrays;
import java.util.List;

public class StartCommand implements Command{
    private TelegramBot bot;

    public StartCommand(TelegramBot bot) {
        this.bot = bot;
    }
    @Override
    public boolean execute(Update update) {
        long chatId = update.getMessage().getChatId();
        if(bot.getUserManager().getUser(chatId) == null) {
            User user = new User();
            user.setRole(Role.UNAUTHORIZED_USER.toString());
            user.setChatId(chatId);
            bot.getUserManager().saveUser(user);
            bot.sendMessage(chatId,"Добро пожаловать!");
            return true;
        }
        bot.sendMessage(chatId,"Добро пожаловать)");
        return true;
    }

    @Override
    public List<String> getLabels() {
        return Arrays.asList(Constants.START_COMMAND);
    }
}
