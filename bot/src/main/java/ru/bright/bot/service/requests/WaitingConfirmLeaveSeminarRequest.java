package ru.bright.bot.service.requests;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.keyboards.confirm.InlineLeaveSeminarConfirmKeyboard;


public class WaitingConfirmLeaveSeminarRequest extends BaseRequest {

    private ScienceSeminar seminar;
    public WaitingConfirmLeaveSeminarRequest(TelegramBot bot, User user, ScienceSeminar seminar) {
        super(bot, user);
        this.seminar = seminar;
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(),"Вы действительно хотите покинуть семинар *" + seminar.getName() + "*?",
                new InlineLeaveSeminarConfirmKeyboard(seminar.getId()),"Markdown");
    }

    @Override
    public boolean handle(Update update) {
        return true;
    }
}
