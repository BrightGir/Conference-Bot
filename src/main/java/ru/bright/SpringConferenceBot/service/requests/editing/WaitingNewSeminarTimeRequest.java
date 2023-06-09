package ru.bright.SpringConferenceBot.service.requests.editing;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.requests.BaseRequest;
import ru.bright.SpringConferenceBot.service.requests.seminar.WaitingSeminarCategoryRequest;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

public class WaitingNewSeminarTimeRequest extends BaseRequest {

    private ScienceSeminar seminar;
    public WaitingNewSeminarTimeRequest(TelegramBot bot, User user, ScienceSeminar seminar) {
        super(bot, user);
        this.seminar = seminar;
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Введите дату семинара в формате [d MMMM yyyy HH:mm] либо [dd.MM.yyyy HH:mm] (МСК) \n" +
                "Например: 18 мая 2023 18:30 или 18.05.2023 18:30");
    }

    @Override
    public boolean handle(Update update) {
        String dateString = "10 января 2020 18:30";
        String format = "d MMMM yyyy HH:mm";
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        SimpleDateFormat sdf = new SimpleDateFormat(format, new java.util.Locale("ru"));
        try {
            Date date = sdf.parse(update.getMessage().getText());
            Timestamp timestamp = new Timestamp(date.getTime());
            checkAndSendRequest(timestamp,seminar,update.getMessage().getChatId());
            return true;
        } catch (ParseException e) {
            try {
                sdf = sdf2;
                sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
                Date date = sdf.parse(update.getMessage().getText());
                Timestamp timestamp2 = new Timestamp(date.getTime());
                checkAndSendRequest(timestamp2,seminar,update.getMessage().getChatId());
            } catch (ParseException ex) {
                getBot().sendMessage(getUser().getChatId(), "Неправильный формат даты. Используйте [d MMMM yyyy HH:mm] либо [dd.MM.yyyy HH:mm] (МСК) \n" +
                        "Например: 18 мая 2023 18:30 или 18.05.2023 18:30");
            }
            return false;
        }
    }

    private void checkAndSendRequest(Timestamp timestamp, ScienceSeminar seminar, long chatId) {
        if(getDelta(timestamp.getTime()) < 0) {
            getBot().sendMessage(getUser().getChatId(), "Введите корректную дату");
            return;
        }
        seminar.setTimestamp(timestamp);
        getBot().getSeminarsManager().saveSeminar(seminar);
        getBot().sendMessage(chatId,"Вы изменили дату семинара!");
    }

    private long getDelta(long date) {
        if(ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli() >= date) {
            return date-ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli();
        }
        return date-ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli();
    }
}
