package ru.bright.bot.service.requests.seminar;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.requests.BaseRequest;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class WaitingSeminarTimeRequest extends BaseRequest {
    private SeminarDTO seminar;
    public WaitingSeminarTimeRequest(TelegramBot bot, User user, SeminarDTO seminar) {
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
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        sdf2.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        SimpleDateFormat sdf = new SimpleDateFormat(format, new java.util.Locale("ru"));
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
            Date date = sdf.parse(update.getMessage().getText());
            Timestamp timestamp = new Timestamp(date.getTime());
         //  seminar.setTimestamp(timestamp);
         //  getBot().sendRequest(new WaitingSeminarCategoryRequest(getBot(),getUser(), seminar),5);
            checkAndSendRequest(timestamp,seminar);
        } catch (ParseException e) {
            try {
                sdf = sdf2;
                sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
                Date date = sdf.parse(update.getMessage().getText());
                Timestamp timestamp = new Timestamp(date.getTime());
                checkAndSendRequest(timestamp,seminar);
            } catch (ParseException ex) {
                getBot().sendMessage(getUser().getChatId(), "Неправильный формат даты. Используйте [d MMMM yyyy HH:mm] либо [dd.MM.yyyy HH:mm] (МСК) \n" +
                        "Например: 18 мая 2023 18:30 или 18.05.2023 18:30");
            }
            return false;
        }
        return false;
    }

    private void checkAndSendRequest(Timestamp timestamp, SeminarDTO seminar) {
        if(getDelta(timestamp.getTime()) < 0) {
            getBot().sendMessage(getUser().getChatId(), "Введите корректную дату");
            return;
        }
        seminar.setTimestamp(timestamp);
        getBot().sendRequest(new WaitingSeminarCategoryRequest(getBot(),getUser(), seminar),5);
    }

    private long getDelta(long date) {
        if(ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli() >= date) {
            return date-ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli();
        }
        return date-ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli();
    }
}
