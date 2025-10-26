package ru.bright.bot.service.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bright.bot.model.*;
import ru.bright.bot.model.dto.SeminarDTO;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserNotificationManager {


    private final UserNotificationRepository userNotificationRepository;
    private final SeminarRepository seminarRepository;

    public UserNotificationManager(@Autowired UserNotificationRepository userNotificationRepository,
                                   @Autowired SeminarRepository seminarRepository) {
        this.userNotificationRepository = userNotificationRepository;
        this.seminarRepository = seminarRepository;
    }

    public List<UserNotification> getUserNotificationsBySeminar(long seminarId, Long userChatId) {
        return userNotificationRepository.findBySeminarAndUser(seminarId, userChatId);
    }

    public void deleteNotification(UserNotification un) {
        userNotificationRepository.deleteById(un.getId());
    }

    public List<UserNotification> getExpiredNotifications(Timestamp timeStamp) {
        return userNotificationRepository.findActiveNotifications(timeStamp);
    }


    public void changeTimeNotify(User user, SeminarDTO seminar, int hour) {
        boolean f = false;
        for(UserNotification notify: getUserNotificationsBySeminar(seminar.getId(), user.getChatId())) {
            if(notify.getHour() == hour) {
                if(notify.isNotified()) {
                    return;
                } else {
                    notify.setActive(!notify.isActive());
                    userNotificationRepository.save(notify);
                }
                f = true;
            }
        }
        if(!f) {
            UserNotification notify = new UserNotification();
            notify.setNotified(false);
            notify.setActive(true);
            notify.setHour(hour);
            notify.setSeminar(seminarRepository.getReferenceById(seminar.getId()));
            notify.setUser(user);
            userNotificationRepository.save(notify);
        }
    }

    public boolean hasNotify(User user, SeminarDTO seminar, int hour) {
        for(UserNotification notification: getUserNotificationsBySeminar(seminar.getId(), user.getChatId())) {
            if(notification.getHour() == hour) {
                if(notification.isNotified()) {
                    return false;
                }
                return notification.isActive();
            }
        }
        return false;
    }

}
