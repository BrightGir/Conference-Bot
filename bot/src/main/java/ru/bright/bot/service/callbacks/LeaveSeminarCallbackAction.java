package ru.bright.bot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.requests.WaitingConfirmLeaveSeminarRequest;

public class LeaveSeminarCallbackAction implements CallbackAction {

    private TelegramBot bot;


    public LeaveSeminarCallbackAction(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void action(Update update, User user) {
        long id = Long.parseLong(update.getCallbackQuery().getData().split("_")[2]);;
        SeminarDTO seminar = bot.getSeminarsManager().findById(id);
        bot.sendRequest(new WaitingConfirmLeaveSeminarRequest(bot,user,seminar),5);
    }

}
