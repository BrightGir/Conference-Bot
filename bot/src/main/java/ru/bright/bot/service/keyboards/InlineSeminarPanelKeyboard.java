package ru.bright.bot.service.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.bot.service.callbacks.CallBackDates;

import java.util.ArrayList;
import java.util.List;

public class InlineSeminarPanelKeyboard extends InlineKeyboardMarkup {

    public InlineSeminarPanelKeyboard(long seminarId) {
        init(seminarId);
    }


    public void init(long seminarId) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton but1 = new InlineKeyboardButton();
        but1.setCallbackData(CallBackDates.LEAVE_SEMINAR.toString() + "_" + seminarId);
        but1.setText("Покинуть семинар");

        InlineKeyboardButton but2 = new InlineKeyboardButton();
        but2.setCallbackData(CallBackDates.SEMINAR_NOTIFIES.toString() + "_" + seminarId);
        but2.setText("Уведомления");

        InlineKeyboardButton but3 = new InlineKeyboardButton();
        but3.setCallbackData(CallBackDates.SEMINAR_TIME_NOTIFIES.toString() + "_" + seminarId);
        but3.setText("Настройка напоминаний");


        row1.add(but1);
        row1.add(but2);
        row2.add(but3);

        rows.add(row1);
        rows.add(row2);
        this.setKeyboard(rows);
    }
}
