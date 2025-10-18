package ru.bright.bot.service.callbacks;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.ApplicationContextProvider;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.keyboards.InlineSeminarCategoryKeyboard;

public class BackToCategoriesCallbackAction implements CallbackAction{

    private TelegramBot bot;
    private InlineSeminarCategoryKeyboard keyboard;

    public BackToCategoriesCallbackAction(TelegramBot bot) {
        this.bot = bot;
        this.keyboard = ApplicationContextProvider.getApplicationContext().getBean(InlineSeminarCategoryKeyboard.class);
    }
    @Override
    public void action(Update update, User user) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText("Выберите категорию");
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setReplyMarkup(keyboard);
        bot.executeMethod(editMessageText);
    }
}
