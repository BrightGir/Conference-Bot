package ru.bright.SpringConferenceBot.service.seminars;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.UserNotification;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.utils.Corrector;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class SeminarScheduler {


    @Autowired
    private TelegramBot bot;

    //  @Scheduled(cron = "0 0 * * * *")

    @Scheduled(cron = "0 */5 * ? * *")
    //@Scheduled(cron = "*/1 * * * * *")
    public void myTask() {
        List<ScienceSeminar> seminarsToDelete = new ArrayList<>();
        for(ScienceSeminar seminar: bot.getSeminarsManager().getAllSeminars()) {
            long milli = getDelta(seminar.getTimestamp().getTime());
            if(milli < 0) {
                seminarsToDelete.add(seminar);
            }
            long seconds = milli/1000;
            long hours = (seconds/60)/60;


            Iterator it = seminar.getNotifications().iterator();
            while(it.hasNext()) {
                UserNotification notify = (UserNotification) it.next();
                if(notify.isNotified()) {
                    continue;
                }
                if(!notify.isActive()) {
                    continue;
                }
                if(hours <= notify.getHour()) {
                    ScienceSeminar s = notify.getSeminar();
                    String cor = Corrector.correct(notify.getHour(),"час","часа","часов");
                    bot.sendMessage(notify.getUser().getChatId(),"Семинар *" + s.getName() + "* начнется через " + notify.getHour() + " " + cor,"Markdown");

                    notify.setNotified(true);
                    bot.getSeminarsManager().updateNotify(notify);
                }
            }

        }
        for(ScienceSeminar seminar: seminarsToDelete) {
            bot.getSeminarsManager().deleteSeminar(seminar);
        }
    }


    private long getDelta(long date) {
        if(ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli() >= date) {
            return date-ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli();
        }
        return date-ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli();
    }
}
