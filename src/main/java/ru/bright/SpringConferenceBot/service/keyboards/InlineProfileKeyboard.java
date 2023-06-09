package ru.bright.SpringConferenceBot.service.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.callbacks.CallBackDates;
import ru.bright.SpringConferenceBot.service.seminars.SeminarCategory;

import java.util.ArrayList;
import java.util.List;

public class InlineProfileKeyboard extends InlineKeyboardMarkup {

    private User user;

    public InlineProfileKeyboard(User user) {
        this.user = user;
        init();
    }


    public void init() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton but1 = new InlineKeyboardButton();
        String but1Text = (user.getFIO() == null) ? "Ввести ФИО" : "Изменить ФИО";
        but1.setText(but1Text);
        but1.setCallbackData(CallBackDates.SET_FIO.toString());

        row.add(but1);
        rows.add(row);
        this.setKeyboard(rows);
    }
}
