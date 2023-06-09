package ru.bright.SpringConferenceBot.service.requests;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.keyboards.confirm.InlineDeleteSeminarConfirmKeyboard;

public class WaitingConfirmSeminarDeleteRequest extends BaseRequest{

    private ScienceSeminar seminar;

    public WaitingConfirmSeminarDeleteRequest(ScienceSeminar seminar, TelegramBot bot, User user) {
        super(bot, user);
        this.seminar = seminar;
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Вы действительно хотите удалить семинар *" + seminar.getName() + "*?",
                new InlineDeleteSeminarConfirmKeyboard(seminar.getId()),"Markdown");
    }

    @Override
    public boolean handle(Update update) {
        return false;
    }
}
