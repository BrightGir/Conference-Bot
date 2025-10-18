package ru.bright.bot.service.callbacks;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.bot.ApplicationContextProvider;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.keyboards.InlinePagesKeyboard;
import ru.bright.bot.service.seminars.SeminarsPagesStore;

import java.util.Arrays;
import java.util.List;


public class CategoryCallbackAction implements CallbackAction {


    private TelegramBot telegramBot;

    private SeminarsPagesStore categoryPagesStore;


    private String category;

    public CategoryCallbackAction(String category, TelegramBot telegramBot) {
        this.category = category;
        this.categoryPagesStore = ApplicationContextProvider.getApplicationContext().getBean(SeminarsPagesStore.class);
        this.telegramBot = telegramBot;
    }

    @Override
    public void action(Update update, User user) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(categoryPagesStore.getPage(category,1));
        editMessageText.setParseMode("Markdown");
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        InlinePagesKeyboard keyboard = new InlinePagesKeyboard(1,categoryPagesStore.getMaxPages(category), CallBackDates.SEMINARS.toString() + "_" + category);
        List<List<InlineKeyboardButton>> rows = keyboard.getKeyboard();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Назад ко всем категориям");
        button.setCallbackData(CallBackDates.BACK_TO_CATEGORIES.toString());
        rows.add(Arrays.asList(button));
        keyboard.setKeyboard(rows);
        editMessageText.setReplyMarkup(keyboard);
        telegramBot.executeMethod(editMessageText);

    }

}
