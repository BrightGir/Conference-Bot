package ru.bright.bot.service.callbacks;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.keyboards.InlinePagesKeyboard;

import java.util.*;
import java.util.function.BiConsumer;

@Slf4j
public class SeminarNotifiesCallbackAction implements CallbackAction{

    private TelegramBot bot;
    private static int MAX_LINES = 15;

    private Map<Long,List<String>> pagesMap;

    public SeminarNotifiesCallbackAction(TelegramBot bot) {
        this.bot = bot;
        this.pagesMap = new HashMap<>();
    }


    @Override
    public void action(Update update, User user) {
        long id = Long.parseLong(update.getCallbackQuery().getData().split("_")[2]);
        SeminarDTO seminar = bot.getSeminarsManager().findById(id);
        List<String> pags = createPages(seminar);
        pagesMap.put(id,pags);
        InlinePagesKeyboard keyboard1 = new InlinePagesKeyboard(1,pags.size(),CallBackDates.SEMINAR_NOTIFIES_PAGE.toString() + "_" + id);
        List<List<InlineKeyboardButton>> rows1 = keyboard1.getKeyboard();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setCallbackData(CallBackDates.BACK_TO_PANEL.toString() + "_" + id);
        button1.setText("Назад к панели");
        rows1.add(Arrays.asList(button1));
        keyboard1.setKeyboard(rows1);

        EditMessageText editMessageText1 = new EditMessageText();
        editMessageText1.setChatId(update.getCallbackQuery().getMessage().getChatId());
        editMessageText1.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText1.setReplyMarkup(keyboard1);
        editMessageText1.setText(pags.get(0));
        bot.executeMethod(editMessageText1);

        bot.registerCallbackData(CallBackDates.SEMINAR_NOTIFIES_PAGE.toString(), (BiConsumer<Update, User>) (upd, u) -> {
            long id2 = Long.parseLong(upd.getCallbackQuery().getData().split("_")[3]);
            List<String> pages = pagesMap.get(id2);
            String[] data = upd.getCallbackQuery().getData().split("_");
            int page = Integer.parseInt(data[4]);
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText(pages.get(page-1));
            editMessageText.setParseMode("Markdown");
            editMessageText.setChatId(upd.getCallbackQuery().getMessage().getChatId());
            editMessageText.setMessageId(upd.getCallbackQuery().getMessage().getMessageId());
            InlinePagesKeyboard keyboard = new InlinePagesKeyboard(page, pages.size(), CallBackDates.SEMINAR_NOTIFIES_PAGE.toString() + "_" + id2);
            List<List<InlineKeyboardButton>> rows = keyboard.getKeyboard();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setCallbackData(CallBackDates.BACK_TO_PANEL.toString() + "_" + id2);
            button.setText("Назад к панели");
            rows.add(Arrays.asList(button));
            keyboard.setKeyboard(rows);
            editMessageText.setReplyMarkup(keyboard);
            bot.executeMethod(editMessageText);
        });


    }


    private List<String> createPages(SeminarDTO seminar) {
        List<String> pages = new ArrayList<>();
        String currentPage = "";
        int str = 0;
        for (String notify : seminar.getMessages()) {
            int addStr = notify.split("\n").length;
            if (str + addStr > MAX_LINES) {
                pages.add(currentPage);
                currentPage = "";
                str = 0;
            }
            currentPage +=  notify;
            currentPage += "\n\n";
            str += addStr;
        }
        if(!currentPage.isEmpty()) {
            pages.add(currentPage);
        } else {
            if(pages.size() == 0) {
                pages.add("Уведомлений нет.");
            }
        }
        return pages;
    }


}
