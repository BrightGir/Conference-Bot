package ru.bright.SpringConferenceBot.service.callbacks.page;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.SpringConferenceBot.ApplicationContextProvider;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.callbacks.CallBackDates;
import ru.bright.SpringConferenceBot.service.callbacks.CallbackAction;
import ru.bright.SpringConferenceBot.service.keyboards.InlinePagesKeyboard;
import ru.bright.SpringConferenceBot.service.seminars.SeminarsPagesStore;

import java.util.Arrays;
import java.util.List;

public class JoinedSeminarsPageCallbackAction implements CallbackAction {

    private TelegramBot bot;

    private SeminarsPagesStore categoryPagesStore;

    public JoinedSeminarsPageCallbackAction(TelegramBot bot) {
        this.categoryPagesStore = ApplicationContextProvider.getApplicationContext().getBean(SeminarsPagesStore.class);
        this.bot = bot;
    }

    @Override
    public void action(Update update, User user) {
        try {
            String[] data = update.getCallbackQuery().getData().split("_");
            int page = Integer.parseInt(data[2]);
            List<String> pages = categoryPagesStore.getJoinedPages(update.getCallbackQuery().getMessage().getChatId());
            InlinePagesKeyboard keyboard = new InlinePagesKeyboard(page, pages.size(), CallBackDates.JOINED_SEMINARS.toString());
            List<List<InlineKeyboardButton>> rows = keyboard.getKeyboard();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setCallbackData(CallBackDates.SEMINAR_PANEL.toString());
            button.setText("Панель семинара");
            rows.add(Arrays.asList(button));
            keyboard.setKeyboard(rows);
            bot.editMessage(update.getCallbackQuery(),pages.get(page-1),keyboard,"Markdown");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
