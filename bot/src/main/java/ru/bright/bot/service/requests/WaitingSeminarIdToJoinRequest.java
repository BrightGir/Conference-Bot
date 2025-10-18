package ru.bright.bot.service.requests;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class WaitingSeminarIdToJoinRequest extends BaseRequest {

    public WaitingSeminarIdToJoinRequest(TelegramBot bot, User user) {
        super(bot, user);
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Введите ID семинара, к которому вы хотите присоединиться");
    }

    @Override
    public boolean handle(Update update) {
        long id;
        try {
            id = Long.parseLong(update.getMessage().getText());
        } catch (NumberFormatException ex) {
            getBot().sendMessage(getUser().getChatId(), "Введите корректный ID");
            return false;
        }
        if(!existSeminarWithId(id)) {
            getBot().sendMessage(getUser().getChatId(), "Семинара с таким ID не существует");
            return false;
        }
        ScienceSeminar seminar = getBot().getSeminarsManager().findById(id);
        if(seminar.getChatIdOwner().longValue() == getUser().getChatId()) {
            getBot().sendMessage(getUser().getChatId(), "Вы не можете присоединиться к своему семинару");
            return false;
        } else if(seminar.getParticipants() != null && seminar.getParticipants().contains(getUser())) {
            getBot().sendMessage(getUser().getChatId(), "Вы уже участвуете в этом семинаре");
            return false;
        }

        ZonedDateTime zdt = seminar.getTimestamp().toInstant()
                .atZone(ZoneId.of("Europe/Moscow"));
        String monthString = zdt.getMonth().getDisplayName(TextStyle.FULL,new Locale("ru"));
        String s = "_" + zdt.getDayOfMonth() + " " + monthString.substring(0,1).toUpperCase() + monthString.substring(1) + " " + zdt.getYear() + " " +
                zdt.getHour()  + ":" + new DecimalFormat("00").format(zdt.getMinute())  + "_";
        getBot().getSeminarsManager().joinTo(getUser(),seminar);
        getBot().sendMessage(getUser().getChatId(), "Вы успешно присоединились к семинару *" + seminar.getName() + "*,\nкоторый пройдет " +
                s,"Markdown");
        return true;
    }

    private boolean existSeminarWithId(long id) {
        return getBot().getSeminarsManager().findById(id) != null;
    }
}
