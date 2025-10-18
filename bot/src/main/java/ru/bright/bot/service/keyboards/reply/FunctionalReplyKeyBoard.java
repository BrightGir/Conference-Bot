package ru.bright.bot.service.keyboards.reply;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.bright.bot.service.utils.Constants;

import java.util.ArrayList;
import java.util.List;

@Component
public class FunctionalReplyKeyBoard extends ReplyKeyboardMarkup {


    public FunctionalReplyKeyBoard() {
        init();
        this.setResizeKeyboard(true);
    }

    private void init() {
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(getButton(Constants.GET_SEMINARS_COMMAND));
        row1.add(getButton(Constants.EDIT_SEMINAR_COMMAND));
        row1.add(getButton(Constants.JOIN_TO_SEMINAR_COMMAND));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(getButton(Constants.JOINED_SEMINARS));
        row2.add(getButton(Constants.INFO_COMMAND));
        row2.add(getButton(Constants.CREATE_SEMINAR_COMMAND));

        rows.add(row1);
        rows.add(row2);
        this.setKeyboard(rows);
    }

    private KeyboardButton getButton(String text) {
        KeyboardButton button = new KeyboardButton();
        button.setText(text);
        return button;
    }
}
