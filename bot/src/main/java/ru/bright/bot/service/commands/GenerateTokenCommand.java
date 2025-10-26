package ru.bright.bot.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;
import ru.bright.bot.service.utils.Constants;
import ru.bright.bot.service.utils.Role;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.requests.WaitingRoleRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GenerateTokenCommand implements Command{

    private TelegramBot bot;
    public GenerateTokenCommand(TelegramBot bot) {
        this.bot = bot;
    }
    @Override
    public boolean execute(Update update) {
        Optional<User> user = Optional.ofNullable(bot.getUserManager().getUser(update.getMessage().getChatId()));
        if(!((user.get().getRole()).getPriority() >= Role.ADMIN.getPriority())) {
            bot.sendMessage(update.getMessage().getChatId(),"Недостаточно прав");
            return true;
        }
        bot.sendRequest(new WaitingRoleRequest(bot,user.get()),3);
        return true;
    }


    @Override
    public List<String> getLabels() {
        return Arrays.asList(Constants.GENERATE_TOKEN_COMMAND);
    }
}
