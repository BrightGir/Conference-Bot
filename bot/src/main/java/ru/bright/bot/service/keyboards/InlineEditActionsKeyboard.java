package ru.bright.bot.service.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.bot.service.callbacks.CallBackDates;

import java.util.ArrayList;
import java.util.List;

public class InlineEditActionsKeyboard extends InlineKeyboardMarkup {

    public InlineEditActionsKeyboard(long seminarId) {
        init(seminarId);
    }


    public void init(long seminarId) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton but1 = new InlineKeyboardButton();
        but1.setCallbackData(CallBackDates.EDIT_TIME.toString() + "_" + seminarId);
        but1.setText("Изменить дату");

        InlineKeyboardButton but2 = new InlineKeyboardButton();
        but2.setCallbackData(CallBackDates.DELETE_CURRENT_SEMINAR.toString() + "_" + seminarId);
        but2.setText("Удалить семинар");

        InlineKeyboardButton but3 = new InlineKeyboardButton();
        but3.setCallbackData(CallBackDates.OWN_SEMINARS_ADDITIONAL_INFORMATION.toString() + "_" + seminarId);
        but3.setText("Доп. информация");

        InlineKeyboardButton but4 = new InlineKeyboardButton();
        but4.setCallbackData(CallBackDates.NOTIFY_PARTICIPANTS.toString() + "_" + seminarId);
        but4.setText("Отправить уведомление");

        row1.add(but1);
        row1.add(but2);
        row2.add(but3);
        row3.add(but4);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        this.setKeyboard(rows);
    }
}
