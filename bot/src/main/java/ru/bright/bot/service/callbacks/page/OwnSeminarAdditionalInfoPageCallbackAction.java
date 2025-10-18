package ru.bright.bot.service.callbacks.page;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.ApplicationContextProvider;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.callbacks.CallBackDates;
import ru.bright.bot.service.callbacks.CallbackAction;
import ru.bright.bot.service.keyboards.InlinePagesKeyboard;
import ru.bright.bot.service.seminars.SeminarsPagesStore;

import java.util.List;

public class OwnSeminarAdditionalInfoPageCallbackAction implements CallbackAction {

    private SeminarsPagesStore categoryPagesStore;
    private TelegramBot bot;

    public OwnSeminarAdditionalInfoPageCallbackAction(TelegramBot bot) {
        this.categoryPagesStore = ApplicationContextProvider.getApplicationContext().getBean(SeminarsPagesStore.class);
        this.bot = bot;
    }

    @Override
    public void action(Update update, User user) {
        try {
            String[] data = update.getCallbackQuery().getData().split("_");
            long id = Integer.parseInt(data[5]);
            int page = Integer.parseInt(data[6]);
            List<String> pages = categoryPagesStore.getSeminarInfoPages(id);
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText(pages.get(page - 1));
            editMessageText.setParseMode("Markdown");
            editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
            editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            InlinePagesKeyboard keyboard = new InlinePagesKeyboard(page, pages.size(),
                    CallBackDates.OWN_SEMINARS_ADDITIONAL_INFORMATION_PAGE.toString() + "_" + id);
            //   List<List<InlineKeyboardButton>> rows = keyboard.getKeyboard();
            //    InlineKeyboardButton button = new InlineKeyboardButton();
            //    button.setCallbackData(CallBackDates.EDIT_SEMINAR.toString());
            //    button.setText("Изменить");
            //    rows.add(Arrays.asList(button));
            //    keyboard.setKeyboard(rows);
            editMessageText.setReplyMarkup(keyboard);
            bot.executeMethod(editMessageText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
