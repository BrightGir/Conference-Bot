package ru.bright.SpringConferenceBot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.keyboards.InlineSeminarTimeNotifiesKeyboard;

public class SeminarTimeNotifiesCallbackAction implements CallbackAction {

    private TelegramBot bot;

    public SeminarTimeNotifiesCallbackAction(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void action(Update update, User user) {
        long seminarId = Long.parseLong(update.getCallbackQuery().getData().split("_")[3]);
        bot.editMessage(update.getCallbackQuery(),"Напомнить о семинаре за:",new InlineSeminarTimeNotifiesKeyboard(bot,seminarId,user));
    }


}
