package ru.bright.bot.service.callbacks.page;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.bot.ApplicationContextProvider;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.callbacks.CallBackDates;
import ru.bright.bot.service.callbacks.CallbackAction;
import ru.bright.bot.service.keyboards.InlinePagesKeyboard;
import ru.bright.bot.service.seminars.SeminarsPagesStore;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class SeminarsPageByCategoryCallbackAction implements CallbackAction {

    private String category;
    private TelegramBot bot;

    private SeminarsPagesStore categoryPagesStore;

    public SeminarsPageByCategoryCallbackAction(TelegramBot bot, String category) {
        this.category = category;
        this.categoryPagesStore = ApplicationContextProvider.getApplicationContext().getBean(SeminarsPagesStore.class);
        this.bot = bot;
    }

    @Override
    public void action(Update update, User user) {
        try {
            String[] data = update.getCallbackQuery().getData().split("_");
            int page = Integer.parseInt(data[2]);
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText(categoryPagesStore.getPage(category, page));
            editMessageText.setParseMode("Markdown");
            editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
            editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            InlinePagesKeyboard keyboard = new InlinePagesKeyboard(page, categoryPagesStore.getMaxPages(category), CallBackDates.SEMINARS.toString() + "_" + category);
            List<List<InlineKeyboardButton>> rows = keyboard.getKeyboard();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setCallbackData(CallBackDates.BACK_TO_CATEGORIES.toString());
            button.setText("Назад ко всем категориям");
            rows.add(Arrays.asList(button));
            keyboard.setKeyboard(rows);
            editMessageText.setReplyMarkup(keyboard);
            bot.executeMethod(editMessageText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
