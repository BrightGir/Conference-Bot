package ru.bright.SpringConferenceBot.service.callbacks;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.ApplicationContextProvider;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.keyboards.InlinePagesKeyboard;
import ru.bright.SpringConferenceBot.service.seminars.SeminarsPagesStore;

import java.util.List;

public class OwnSeminarAdditionalInfoCallbackAction implements CallbackAction{

    private TelegramBot bot;
    private SeminarsPagesStore seminarsPagesStore;
    public OwnSeminarAdditionalInfoCallbackAction(TelegramBot bot) {
        this.bot = bot;
        this.seminarsPagesStore = ApplicationContextProvider.getApplicationContext().getBean(SeminarsPagesStore.class);
    }


    @Override
    public void action(Update update, User user) {
        String msg = update.getCallbackQuery().getData().split("_")[4];
        Long id = null;
        try {
            id = Long.parseLong(msg);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        ScienceSeminar seminar = bot.getSeminarsManager().getSeminarByPrimaryId(id);
        List<String> pages = seminarsPagesStore.getSeminarInfoPages(seminar.getId());
        bot.sendMessage(user.getChatId(), pages.get(0),new InlinePagesKeyboard(1,pages.size(),
                CallBackDates.OWN_SEMINARS_ADDITIONAL_INFORMATION_PAGE + "_" + seminar.getId()),"Markdown");
    }


}
