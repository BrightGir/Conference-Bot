package ru.bright.bot.service.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.callbacks.CallBackDates;

import java.util.ArrayList;
import java.util.List;

public class InlineSeminarTimeNotifiesKeyboard extends InlineKeyboardMarkup {

    private long seminarId;
    private TelegramBot bot;
    private User user;

    public InlineSeminarTimeNotifiesKeyboard(TelegramBot bot, long seminarId, User user) {
        this.seminarId = seminarId;
        this.bot = bot;
        this.user = user;
        init();
    }

    public void init() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        String add = " ✅";
        ScienceSeminar seminar = bot.getSeminarsManager().findById(seminarId);

        InlineKeyboardButton but1 = new InlineKeyboardButton();
        but1.setCallbackData(CallBackDates.TIME_NOTIFY.toString() + "_" + seminarId + "_" + "1");
        but1.setText("1 час" + (bot.getUserNotificationManager().hasNotify(user,seminar,1) ? add : ""));

        InlineKeyboardButton but2 = new InlineKeyboardButton();
        but2.setCallbackData(CallBackDates.TIME_NOTIFY.toString() + "_" + seminarId + "_" + "3");
        but2.setText("3 часа"+ (bot.getUserNotificationManager().hasNotify(user,seminar,3) ? add : ""));

        InlineKeyboardButton but3 = new InlineKeyboardButton();
        but3.setCallbackData(CallBackDates.TIME_NOTIFY.toString() + "_" + seminarId + "_" + "6");
        but3.setText("6 часов"+ (bot.getUserNotificationManager().hasNotify(user,seminar,6) ? add : ""));

        InlineKeyboardButton but4 = new InlineKeyboardButton();
        but4.setCallbackData(CallBackDates.TIME_NOTIFY.toString() + "_" + seminarId + "_" + "12");
        but4.setText("12 часов"+ (bot.getUserNotificationManager().hasNotify(user,seminar,12) ? add : ""));

        InlineKeyboardButton but5 = new InlineKeyboardButton();
        but5.setCallbackData(CallBackDates.TIME_NOTIFY.toString() + "_" + seminarId + "_" + "16");
        but5.setText("16 часов" + (bot.getUserNotificationManager().hasNotify(user,seminar,16) ? add : ""));

        InlineKeyboardButton but6 = new InlineKeyboardButton();
        but6.setCallbackData(CallBackDates.TIME_NOTIFY.toString() + "_" + seminarId + "_" + "24");
        but6.setText("24 часа" + (bot.getUserNotificationManager().hasNotify(user,seminar,24) ? add : ""));



        InlineKeyboardButton back_button = new InlineKeyboardButton();
        back_button.setCallbackData(CallBackDates.BACK_TO_PANEL.toString() + "_" + seminarId);
        back_button.setText("Назад к панели");

        row1.add(but1);
        row1.add(but2);
        row2.add(but3);
        row2.add(but4);
        row3.add(but5);
        row4.add(back_button);
        row3.add(but6);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        this.setKeyboard(rows);
    }
}
