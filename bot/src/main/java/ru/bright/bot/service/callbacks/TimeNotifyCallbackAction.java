package ru.bright.bot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.keyboards.InlineSeminarTimeNotifiesKeyboard;

public class TimeNotifyCallbackAction implements CallbackAction{

    private TelegramBot bot;

    public TimeNotifyCallbackAction(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void action(Update update, User user) {
        long seminarId = Long.parseLong(update.getCallbackQuery().getData().split("_")[2]);
        int hour = Integer.parseInt(update.getCallbackQuery().getData().split("_")[3]);
        bot.getUserNotificationManager().changeTimeNotify(user,bot.getSeminarsManager().findById(seminarId),hour);
        bot.editMessage(update.getCallbackQuery(),"Напомнить о семинаре за:",new InlineSeminarTimeNotifiesKeyboard(bot,seminarId,user));
    }

}
