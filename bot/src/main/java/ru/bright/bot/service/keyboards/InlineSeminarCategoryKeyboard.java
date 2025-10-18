package ru.bright.bot.service.keyboards;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.bright.bot.service.seminars.SeminarCategory;

import java.util.ArrayList;
import java.util.List;


@Scope("singleton")
@Component
public class InlineSeminarCategoryKeyboard extends InlineKeyboardMarkup {

    public InlineSeminarCategoryKeyboard() {
        init();
    }

    private void init() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        int k = 0;
        for (SeminarCategory category: SeminarCategory.values()) {
            if(k == 2) {
                k = 0;
                rows.add(row);
                row = new ArrayList<>();
            }

            InlineKeyboardButton but = new InlineKeyboardButton();
            but.setText(category.getName());
            but.setCallbackData(category.toString());
            row.add(but);
            k++;
        }
        if(!row.isEmpty()) {
            rows.add(row);
        }
        this.setKeyboard(rows);
    }


}
