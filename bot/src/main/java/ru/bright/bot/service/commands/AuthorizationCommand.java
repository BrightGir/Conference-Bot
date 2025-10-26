package ru.bright.bot.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;
import ru.bright.bot.service.requests.token.WaitingTokenRequest;
import ru.bright.bot.service.utils.Constants;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.utils.Role;

import java.util.Arrays;
import java.util.List;

public class AuthorizationCommand implements Command{

    private TelegramBot bot;
    public AuthorizationCommand(TelegramBot bot) {
        this.bot = bot;
    }
    @Override
    public boolean execute(Update update) {
        User user = bot.getUserManager().getUser(update.getMessage().getChatId());
        if(user.getRole() == Role.UNAUTHORIZED_USER) {
            bot.sendRequest(new WaitingTokenRequest(bot, user), 5);
        }
        return true;
    }

    @Override
    public List<String> getLabels() {
        return Arrays.asList(Constants.AUTHORIZATION_COMMAND);
    }
}
