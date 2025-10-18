package ru.bright.bot.service.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.model.UserNotification;
import ru.bright.bot.model.UserNotificationRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserNotificationManager {

    private final UserNotificationRepository userNotificationRepository;

    public UserNotificationManager(@Autowired UserNotificationRepository userNotificationRepository) {
        this.userNotificationRepository = userNotificationRepository;
    }

    @CacheEvict(value = "notifications", key = "#un.seminar.id + '_' + #un.user.chatId")
    public UserNotification saveNotify(UserNotification un) {
        UserNotification not = userNotificationRepository.save(un);
        return not;
    }

    @Cacheable(value = "notifications", key = "#seminar.id + '_' + #user.chatId")
    public List<UserNotification> getUserNotificationsBySeminar(ScienceSeminar seminar, User user) {
        return userNotificationRepository.findBySeminarAndUser(seminar, user);
    }

    @CacheEvict(value = "notifications", key = "#un.seminar.id + '_' + #un.user.chatId")
    public void deleteNotification(UserNotification un) {
        userNotificationRepository.deleteById(un.getId());
    }

    public List<UserNotification> getExpiredNotifications(Timestamp timeStamp) {
        return userNotificationRepository.findActiveNotifications(timeStamp);
    }


    public void changeTimeNotify(User user, ScienceSeminar seminar, int hour) {
        boolean f = false;
        for(UserNotification notify: getUserNotificationsBySeminar(seminar,user)) {
            if(notify.getHour() == hour) {
                if(notify.isNotified()) {
                    return;
                } else {
                    notify.setActive(!notify.isActive());
                    saveNotify(notify);
                }
                f = true;
            }
        }
        if(!f) {
            UserNotification notify = new UserNotification();
            notify.setNotified(false);
            notify.setActive(true);
            notify.setHour(hour);
            notify.setSeminar(seminar);
            notify.setUser(user);
            saveNotify(notify);
        }
    }

    public boolean hasNotify(User user, ScienceSeminar seminar, int hour) {
        for(UserNotification notification: getUserNotificationsBySeminar(seminar,user)) {
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
