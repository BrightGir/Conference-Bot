package ru.bright.SpringConferenceBot.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.keyboards.InlineSeminarCategoryKeyboard;
import ru.bright.SpringConferenceBot.service.seminars.SeminarCategory;
import ru.bright.SpringConferenceBot.service.utils.Constants;

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
