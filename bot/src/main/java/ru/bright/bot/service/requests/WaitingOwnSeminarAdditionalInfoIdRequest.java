package ru.bright.bot.service.requests;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.ApplicationContextProvider;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.callbacks.CallBackDates;
import ru.bright.bot.service.keyboards.InlinePagesKeyboard;
import ru.bright.bot.service.seminars.SeminarsPagesStore;

import java.util.List;

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
        SeminarDTO seminar = getBot().getSeminarsManager().findById(id);
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
