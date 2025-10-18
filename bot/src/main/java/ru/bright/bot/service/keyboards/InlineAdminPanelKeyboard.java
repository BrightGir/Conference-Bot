package ru.bright.bot.service.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.bot.service.callbacks.CallBackDates;

import java.util.ArrayList;
import java.util.List;

public class InlineAdminPanelKeyboard extends InlineKeyboardMarkup {

    public InlineAdminPanelKeyboard() {
        init();
    }

    public void init() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton but1 = new InlineKeyboardButton();
        but1.setCallbackData(CallBackDates.DELETE_SEMINAR.toString());
        but1.setText("Удалить семинар ❌");

        row1.add(but1);
        rows.add(row1);
        this.setKeyboard(rows);
    }
}
