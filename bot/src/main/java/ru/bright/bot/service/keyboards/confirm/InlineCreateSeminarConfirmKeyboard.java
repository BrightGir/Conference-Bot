package ru.bright.bot.service.keyboards.confirm;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.bot.service.callbacks.CallBackDates;

import java.util.ArrayList;
import java.util.List;

public class InlineCreateSeminarConfirmKeyboard extends InlineKeyboardMarkup {

    public InlineCreateSeminarConfirmKeyboard(long seminarId) {
        init(seminarId);
    }

    public void init(long seminarId) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton but1 = new InlineKeyboardButton();
        but1.setCallbackData(CallBackDates.CREATE_SEMINAR_CONFIRM.toString() + "_" + seminarId);
        but1.setText("Подтвердить ✅");

        InlineKeyboardButton but2 = new InlineKeyboardButton();
        but2.setCallbackData(CallBackDates.CREATE_SEMINAR_DENY.toString() + "_" + seminarId);
        but2.setText("Удалить ❌");


        row1.add(but1);
        row1.add(but2);
        rows.add(row1);
        this.setKeyboard(rows);
    }
}
