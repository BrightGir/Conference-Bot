package ru.bright.SpringConferenceBot.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.requests.seminar.WaitingSeminarLeaderRequest;
import ru.bright.SpringConferenceBot.service.requests.seminar.WaitingSeminarNameRequest;
import ru.bright.SpringConferenceBot.service.utils.Constants;
import ru.bright.SpringConferenceBot.service.utils.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CreateSeminarCommand implements Command {

    private TelegramBot bot;
    public CreateSeminarCommand(TelegramBot bot) {
        this.bot = bot;
    }


    @Override
    public boolean execute(Update update) {
        Optional<User> optUser = Optional.ofNullable(bot.getUserManager().getUser(update.getMessage().getChatId()));
        if(optUser.isEmpty() || Role.valueOf(optUser.get().getRole()).getPriority() < Role.USER.getPriority()) {
            bot.sendMessage(update.getMessage().getChatId(),"Недостаточно прав");
            return true;
        }
        bot.sendRequest(new WaitingSeminarLeaderRequest(bot,optUser.get()),5);
        return true;
    }


    @Override
    public List<String> getLabels() {
        return Arrays.asList(Constants.CREATE_SEMINAR_COMMAND);
    }
}
