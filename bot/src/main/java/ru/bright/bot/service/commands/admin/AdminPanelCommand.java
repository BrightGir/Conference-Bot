package ru.bright.bot.service.commands.admin;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.commands.Command;
import ru.bright.bot.service.keyboards.InlineAdminPanelKeyboard;
import ru.bright.bot.service.utils.Constants;
import ru.bright.bot.service.utils.Role;

import java.util.Arrays;
import java.util.List;

public class AdminPanelCommand implements Command {

    private TelegramBot bot;

    public AdminPanelCommand(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean execute(Update update) {
        User user = bot.getUserManager().getUser(update.getMessage().getChatId());
        if(!user.getRole().equals(Role.ADMIN.toString())) {
            return true;
        }
        bot.sendMessage(update.getMessage().getChatId(),"Выберите действие",new InlineAdminPanelKeyboard());
        return true;
    }

    @Override
    public List<String> getLabels() {
        return Arrays.asList(Constants.ADMIN_PANEL);
    }
}
