package ru.bright.bot.service.keyboards;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class InlinePagesKeyboard extends InlineKeyboardMarkup {

    private int page;
    private int maxPage;
    private String callbackData;

    // CallBack Data выглядит так: CALLBACKDATA_{PAGE}
    public InlinePagesKeyboard(int page, int maxPage, String callbackData) {
        this.page = page;
        this.maxPage = maxPage;
        this.callbackData = callbackData;
        init();
    }

    private void init() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        if(page <= 3) {
            for(int i = 1; i <= Math.min(maxPage,4); i++) {
                InlineKeyboardButton but = new InlineKeyboardButton();
                but.setCallbackData(callbackData + "_" + i);
                if(page == i) {
                    but.setText("·" + page + "·");
                } else {
                    but.setText(String.valueOf(i));
                }
                row.add(but);
            }
            if(maxPage >= 5) {
                InlineKeyboardButton but = new InlineKeyboardButton();
                but.setCallbackData(callbackData + "_" + maxPage);
                if(maxPage == 5) {
                    but.setText(String.valueOf(maxPage));
                } else {
                    but.setText(maxPage + " ⇛");
                }
                row.add(but);
            }
        } else {
            if(maxPage-page <= 2) {
                InlineKeyboardButton one = new InlineKeyboardButton();
                one.setCallbackData(callbackData + "_1");
                one.setText("⇚ 1");
                row.add(one);

                for(int i = maxPage-3; i <= maxPage; i++) {
                    InlineKeyboardButton but1 = new InlineKeyboardButton();
                    but1.setCallbackData(callbackData + "_" + (i));
                    if(page == i) {
                        but1.setText("·" + page + "·");
                    } else {
                        but1.setText(String.valueOf(i));
                    }
                    row.add(but1);
                }
            } else {
                InlineKeyboardButton one = new InlineKeyboardButton();
                one.setCallbackData(callbackData + "_1");
                one.setText("⇚ 1");
                row.add(one);

                InlineKeyboardButton but1 = new InlineKeyboardButton();
                but1.setCallbackData(callbackData + "_" + (page - 1));
                but1.setText(String.valueOf(page - 1));
                row.add(but1);

                InlineKeyboardButton but2 = new InlineKeyboardButton();
                but2.setCallbackData(callbackData + "_" + (page));
                but2.setText("·" + page + "·");
                row.add(but2);

                InlineKeyboardButton but3 = new InlineKeyboardButton();
                but3.setCallbackData(callbackData + "_" + (page + 1));
                but3.setText(String.valueOf(page + 1));
                row.add(but3);

                InlineKeyboardButton but4 = new InlineKeyboardButton();
                but4.setCallbackData(callbackData + "_" + maxPage);
                if (page + 2 == maxPage) {
                    but4.setText(String.valueOf(maxPage));
                } else {
                    but4.setText(maxPage + " ⇛");
                }
                row.add(but4);
            }
        }
        rows.add(row);
        this.setKeyboard(rows);
    }

}
