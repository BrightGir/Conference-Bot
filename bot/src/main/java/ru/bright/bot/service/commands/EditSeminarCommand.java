package ru.bright.bot.service.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.bot.ApplicationContextProvider;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.callbacks.CallBackDates;
import ru.bright.bot.service.keyboards.InlinePagesKeyboard;
import ru.bright.bot.service.utils.Constants;
import ru.bright.bot.service.seminars.SeminarsPagesStore;

import java.util.Arrays;
import java.util.List;

public class EditSeminarCommand implements Command{

    private TelegramBot bot;
    private SeminarsPagesStore seminarsPagesStore;

    public EditSeminarCommand(TelegramBot bot) {
        this.seminarsPagesStore = ApplicationContextProvider.getApplicationContext().getBean(SeminarsPagesStore.class);
        this.bot = bot;
    }

    @Override
    public boolean execute(Update update) {
        List<String> pages = seminarsPagesStore.getPagesByOwnerId(update.getMessage().getChatId());
        if(bot.getSeminarsManager().getSeminarsByChatIdOwner(update.getMessage().getChatId()).size() != 0) {
            bot.sendMessage(update.getMessage().getChatId(), "Ваши семинары:");
        }
        InlinePagesKeyboard keyboard = new InlinePagesKeyboard(1,pages.size(),
                CallBackDates.OWNSEMINARS.toString());
        List<List<InlineKeyboardButton>> rows = keyboard.getKeyboard();
      //  InlineKeyboardButton button = new InlineKeyboardButton();
      //  button.setCallbackData(CallBackDates.EDIT_SEMINAR.toString());
      //  button.setText("Изменить");
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setCallbackData(CallBackDates.MANAGE_SEMINAR.toString());
        button.setText("Управлять семинаром");
      //  InlineKeyboardButton button2 = new InlineKeyboardButton();
      //  button2.setCallbackData(CallBackDates.OWN_SEMINARS_ADDITIONAL_INFORMATION.toString());
      //  button2.setText("Доп. информация");
        rows.add(Arrays.asList(button));
        if(bot.getSeminarsManager().getSeminarsByChatIdOwner(update.getMessage().getChatId()).size() != 0) {
            keyboard.setKeyboard(rows);
            bot.sendMessage(update.getMessage().getChatId(), pages.get(0), keyboard, "Markdown");
        } else {
            bot.sendMessage(update.getMessage().getChatId(), pages.get(0), "Markdown");
        }
        return true;
    }


    @Override
    public List<String> getLabels() {
        return Arrays.asList(Constants.EDIT_SEMINAR_COMMAND);
    }
}
