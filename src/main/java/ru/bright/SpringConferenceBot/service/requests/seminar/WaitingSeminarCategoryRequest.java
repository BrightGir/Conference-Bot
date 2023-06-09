package ru.bright.SpringConferenceBot.service.requests.seminar;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.requests.BaseRequest;
import ru.bright.SpringConferenceBot.service.requests.WaitingConfirmSeminarCreateRequest;
import ru.bright.SpringConferenceBot.service.seminars.SeminarCategory;

public class WaitingSeminarCategoryRequest extends BaseRequest {

    private ScienceSeminar seminar;

    public WaitingSeminarCategoryRequest(TelegramBot bot, User user, ScienceSeminar seminar) {
        super(bot, user);
        this.seminar = seminar;
    }

    @Override
    public void sendCreateHandleMessage() {
        StringBuilder b = new StringBuilder("Введите категорию семинара. Доступные категории:\n");
        int cou = 1;
        for(SeminarCategory c: SeminarCategory.values()) {
            b.append("\n" + cou + ". " + c.toString() + " - " + c.getName());
            cou += 1;
        }
        getBot().sendMessage(getUser().getChatId(), b.toString());
    }

    @Override
    public boolean handle(Update update) {
        SeminarCategory category = null;
        try {
            category = SeminarCategory.valueOf(update.getMessage().getText());
            seminar.setSeminarCategoryEnum(category.toString());
            // getBot().getSeminarsManager().saveSeminar(seminar);
        //    getBot().sendMessage(getUser().getChatId(),"Семинар успешно создан!");
            getBot().sendRequest(new WaitingConfirmSeminarCreateRequest(getBot(),getUser(),seminar),5);
            return true;
        } catch (IllegalArgumentException e) {
            StringBuilder b = new StringBuilder("Введите корректную категорию семинара. Доступные категории:\n");
            int cou = 1;
            for(SeminarCategory c: SeminarCategory.values()) {
                b.append("\n" + cou + ". " + c.toString() + " - " + c.getName());
                cou += 1;
            }
            getBot().sendMessage(getUser().getChatId(), b.toString());
        }
        return false;
    }
}
