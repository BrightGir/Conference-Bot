package ru.bright.bot.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.keyboards.InlineSeminarCategoryKeyboard;
import ru.bright.bot.service.utils.Constants;

import java.util.Arrays;
import java.util.List;

public class GetSeminarsCommand implements Command {

    private TelegramBot bot;

    public GetSeminarsCommand(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean execute(Update update) {
        bot.sendMessage(update.getMessage().getChatId(),"Выберите категорию",new InlineSeminarCategoryKeyboard());
        return true;
    }

    @Override
    public List<String> getLabels() {
        return Arrays.asList(Constants.GET_SEMINARS_COMMAND);
    }

}
