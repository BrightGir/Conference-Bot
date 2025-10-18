package ru.bright.bot.service.keyboards.reply;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.bright.bot.service.utils.Constants;

import java.util.ArrayList;
import java.util.List;

@Component
public class BaseReplyKeyboard extends ReplyKeyboardMarkup {

    public BaseReplyKeyboard() {
        init();
        this.setResizeKeyboard(true);
    }

    private void init() {
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(getButton(Constants.AUTHORIZATION_COMMAND));
        rows.add(row1);
        this.setKeyboard(rows);
    }

    private KeyboardButton getButton(String text) {
        KeyboardButton button = new KeyboardButton();
        button.setText(text);
        return button;
    }
}
