package ru.bright.bot.service.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.bot.ApplicationContextProvider;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.callbacks.CallBackDates;
import ru.bright.bot.service.keyboards.InlinePagesKeyboard;
import ru.bright.bot.service.seminars.SeminarsPagesStore;
import ru.bright.bot.service.utils.Constants;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class JoinedSeminarsCommand implements Command{


    private TelegramBot bot;
    private SeminarsPagesStore seminarsPagesStore;

    public JoinedSeminarsCommand(TelegramBot bot) {
        this.bot = bot;
        this.seminarsPagesStore = ApplicationContextProvider.getApplicationContext().getBean(SeminarsPagesStore.class);
    }

    @Override
    public boolean execute(Update update) {
        List<String> pages = seminarsPagesStore.getJoinedPages(update.getMessage().getChatId());
        int joinedSeminarsSize = bot.getSeminarsManager().getSeminarsByUserId(update.getMessage().getChatId()).size();
        if(joinedSeminarsSize != 0) {
            bot.sendMessage(update.getMessage().getChatId(), "Ваши семинары: (" + joinedSeminarsSize + ")");
        }
        InlinePagesKeyboard keyboard = new InlinePagesKeyboard(1,pages.size(),
                CallBackDates.JOINED_SEMINARS.toString());
        List<List<InlineKeyboardButton>> rows = keyboard.getKeyboard();
        InlineKeyboardButton button = new InlineKeyboardButton();
         button.setCallbackData(CallBackDates.SEMINAR_PANEL.toString());
         button.setText("Панель семинара");
        rows.add(Arrays.asList(button));
        keyboard.setKeyboard(rows);
        if(joinedSeminarsSize != 0) {
            bot.sendMessage(update.getMessage().getChatId(),pages.get(0),keyboard,"Markdown");
        } else {
            bot.sendMessage(update.getMessage().getChatId(),pages.get(0),"Markdown");
        }
        return true;
    }

    @Override
    public List<String> getLabels() {
        return Arrays.asList(Constants.JOINED_SEMINARS);
    }
}
