package ru.bright.bot.service.seminars;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.UserNotification;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.utils.Corrector;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class SeminarScheduler {

    @Autowired
    private TelegramBot bot;

    @Scheduled(cron = "0 */5 * ? * *")
    public void myTask() {
        ZonedDateTime nowMsk = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        Timestamp currentTime = Timestamp.from(nowMsk.toInstant());

        List<ScienceSeminar> seminarsToDelete = bot.getSeminarsManager()
                .getExpiredSeminars(currentTime);

        seminarsToDelete.forEach(s -> bot.getSeminarsManager().deleteSeminar(s.getId()));

        List<UserNotification> activeNotifications =
                bot.getUserNotificationManager().getExpiredNotifications(currentTime);

        for(UserNotification notify : activeNotifications) {
            ScienceSeminar s = notify.getSeminar();
            long hours = calculateHoursUntil(nowMsk, s.getTimestamp());

            if(hours <= notify.getHour()) {
                String cor = Corrector.plurals(notify.getHour(), "час", "часа", "часов");
                bot.sendMessage(notify.getUser().getChatId(),
                        "Семинар *" + s.getName() + "* начнется через " +
                                notify.getHour() + " " + cor, "Markdown");

                bot.getUserNotificationManager().deleteNotification(notify);
            }
        }
    }

    private long calculateHoursUntil(ZonedDateTime now, Timestamp seminarTime) {
        ZonedDateTime seminarZdt = seminarTime.toInstant().atZone(ZoneId.of("Europe/Moscow"));
        long millis = seminarZdt.toInstant().toEpochMilli() - now.toInstant().toEpochMilli();
        return (millis / 1000 / 60) / 60;
    }

}
