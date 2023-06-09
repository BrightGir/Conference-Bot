package ru.bright.SpringConferenceBot.service.requests;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.ApplicationContextProvider;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.callbacks.CallBackDates;
import ru.bright.SpringConferenceBot.service.keyboards.InlinePagesKeyboard;
import ru.bright.SpringConferenceBot.service.requests.editing.WaitingActionRequest;
import ru.bright.SpringConferenceBot.service.seminars.SeminarsPagesStore;

import java.util.List;
import java.util.function.BiConsumer;

public class WaitingOwnSeminarAdditionalInfoIdRequest extends BaseRequest{

    private SeminarsPagesStore seminarsPagesStore;
    public WaitingOwnSeminarAdditionalInfoIdRequest(TelegramBot bot, User user) {
        super(bot, user);
        this.seminarsPagesStore = ApplicationContextProvider.getApplicationContext().getBean(SeminarsPagesStore.class);
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Введите ID семинара, дополнительную информацию которого вы хотите получить");
    }

    @Override
    public boolean handle(Update update) {
        String msg = update.getMessage().getText();
        Long id;
        try {
            id = Long.parseLong(msg);
        } catch (NumberFormatException e) {
            getBot().sendMessage(update.getMessage().getChatId(),"Введите корректный ID");
            return false;
        }
        ScienceSeminar seminar = getBot().getSeminarsManager().getSeminarByPrimaryId(id);
        if(seminar == null) {
            getBot().sendMessage(update.getMessage().getChatId(),"Семинара с таким ID не существует");
            return false;
        }
        if(seminar.getChatIdOwner().longValue() != getUser().getChatId().longValue()) {
            getBot().sendMessage(update.getMessage().getChatId(),"Вы не являетесь руководителем семинара с таким ID");
            return false;
        }
        List<String> pages = seminarsPagesStore.getSeminarInfoPages(seminar.getId());
        getBot().sendMessage(update.getMessage().getChatId(), pages.get(0),new InlinePagesKeyboard(1,pages.size(),
                CallBackDates.OWN_SEMINARS_ADDITIONAL_INFORMATION_PAGE + "_" + seminar.getId()),"Markdown");
        return true;
    }
}
