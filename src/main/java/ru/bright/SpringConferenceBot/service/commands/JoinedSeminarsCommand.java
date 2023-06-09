package ru.bright.SpringConferenceBot.service.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.SpringConferenceBot.ApplicationContextProvider;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.callbacks.CallBackDates;
import ru.bright.SpringConferenceBot.service.keyboards.InlinePagesKeyboard;
import ru.bright.SpringConferenceBot.service.seminars.SeminarsPagesStore;
import ru.bright.SpringConferenceBot.service.utils.Constants;

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
        int joinedSeminarsSize = bot.getUserManager().getUser(update.getMessage().getChatId()).getJoinedSeminars().size();
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
