package ru.bright.SpringConferenceBot.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.requests.token.WaitingTokenRequest;
import ru.bright.SpringConferenceBot.service.utils.Constants;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.utils.Role;

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
        if(user.getRole().equals(Role.UNAUTHORIZED_USER.toString())) {
            bot.sendRequest(new WaitingTokenRequest(bot, user), 5);
        }
        return true;
    }

    @Override
    public List<String> getLabels() {
        return Arrays.asList(Constants.AUTHORIZATION_COMMAND);
    }
}
