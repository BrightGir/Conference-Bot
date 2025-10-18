package ru.bright.bot.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.keyboards.InlineProfileKeyboard;
import ru.bright.bot.service.utils.Constants;
import ru.bright.bot.service.utils.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InformationCommand implements Command{
    private TelegramBot bot;
    public InformationCommand(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean execute(Update update) {
        User user = update.getMessage().getFrom();
        StringBuilder b = new StringBuilder("Информация о пользователе " + user.getUserName() + "\n");
        Optional<ru.bright.bot.model.User> optCuser = Optional.ofNullable(bot.getUserManager().getUser(update.getMessage().getChatId()));
   
        ru.bright.bot.model.User cUser = optCuser.get();
        b.append("Группа: " + Role.valueOf(cUser.getRole()).getName() + "\n");
        b.append("ФИО: " + ((cUser.getFio() == null) ? "Не определено" : cUser.getFio()));
        bot.sendMessage(update.getMessage().getChatId(),b.toString(),new InlineProfileKeyboard(cUser));
        return true;
    }

    @Override
    public List<String> getLabels() {
        return Arrays.asList(Constants.INFO_COMMAND);
    }
}
