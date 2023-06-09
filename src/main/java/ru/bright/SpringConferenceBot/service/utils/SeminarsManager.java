package ru.bright.SpringConferenceBot.service.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.bright.SpringConferenceBot.model.*;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.seminars.ParticipateUserListener;
import ru.bright.SpringConferenceBot.service.seminars.SeminarCategory;
import ru.bright.SpringConferenceBot.service.seminars.UpdateSeminarsListener;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
@Slf4j
public class SeminarsManager {

    private Map<Long, ScienceSeminar> primarySeminars;

    @PersistenceContext
    private EntityManager entityManager;

    private SeminarRepository seminarRepository;
    private List<UpdateSeminarsListener> listeners;
    private List<ParticipateUserListener> participateListeners;
    private UserNotificationRepository userNotificationRepository;
    private UserManager userManager;



    public SeminarsManager(@Autowired SeminarRepository seminarRepository, @Autowired UserManager userManager, @Autowired UserNotificationRepository userNotificationRepository) {
        this.seminarRepository = seminarRepository;
        this.userManager = userManager;
        this.listeners = new ArrayList<>();
        this.participateListeners = new ArrayList<>();
        this.userNotificationRepository = userNotificationRepository;
        this.primarySeminars = new HashMap<>();
        initSeminars();
    }


    public void joinTo(User user, ScienceSeminar seminar) {
        user.getJoinedSeminars().add(seminar);
        seminar.getParticipants().add(user);
        this.saveSeminar(seminar);
        notifyListeners(user,seminar);
    }

    public void unjoinFrom(User user, ScienceSeminar seminar) {
        user.getJoinedSeminars().remove(seminar);
        seminar.getParticipants().remove(user);
        this.saveSeminar(seminar);
        notifyListeners(user,seminar);
    }

    public void changeTimeNotify(User user, ScienceSeminar seminar, int hour) {
        boolean f = false;
        for(UserNotification notify: getUserNotificationsBySeminar(seminar,user)) {
            if(notify.getHour() == hour) {
                if(notify.isNotified()) {
                    return;
                } else {
                    notify.setActive(!notify.isActive());
                    updateNotify(notify);
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
            addNotify(seminar, notify);
        }
    }

    @Transactional
    public void updateNotify(UserNotification notify) {
        ScienceSeminar seminar = notify.getSeminar();

        // Найти уведомление в списке уведомлений семинара
        UserNotification existingNotify = seminar.getNotifications().stream()
                .filter(n -> n.getId() == notify.getId())
                .findFirst()
                .orElse(null);

        if (existingNotify != null) {

            existingNotify.setNotified(notify.isNotified());
            existingNotify.setActive(notify.isActive());

            saveSeminar(seminar);
        }
    }

    private void addNotify(ScienceSeminar seminar2, UserNotification un) {
        ScienceSeminar seminar = seminarRepository.findById(seminar2.getId()).get();
        List<UserNotification> notifies = seminar.getNotifications();
        notifies.removeIf(
                notify -> (notify.getId() == un.getId())
        );
        seminar.setNotifications(notifies);
        saveSeminar(seminar);
    }

    public void saveSeminar(ScienceSeminar seminar) {
        primarySeminars.put(seminar.getId(),seminar);
        seminarRepository.save(seminar);
        notifyListeners(seminar,true);
    }


    public List<UserNotification> getUserNotificationsBySeminar(ScienceSeminar seminar, User user) {
        return seminar.getNotifications().stream()
                .filter(notification -> notification.getUser().getChatId().equals(user.getChatId()))
                .collect(Collectors.toList());
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

    public void registerUpdateSeminarsListener(UpdateSeminarsListener listener) {
        listeners.add(listener);
    }

    public void registerParticipateUserListener(ParticipateUserListener listener) {
        participateListeners.add(listener);
    }

    public List<ScienceSeminar> getByCategory(String category) {
        return getAllSeminars().stream().filter(seminar -> (seminar.getSeminarCategoryEnum().equals(category)))
                .collect(Collectors.toList());
    }

    public List<ScienceSeminar> getSeminarsByChatIdOwner(Long chatId) {
        return getAllSeminars().stream().filter(seminar -> (seminar.getChatIdOwner().longValue() == chatId.longValue()))
                .collect(Collectors.toList());
    }

    public ScienceSeminar getSeminarByPrimaryId(long id) {
        return primarySeminars.get(id);
    }

    public Long firstAvailableId() {
        return seminarRepository.findFirstAvailableId();
    }

    public void notify(TelegramBot bot, ScienceSeminar seminar, String message) {
        seminar.addMessage(message);
        saveSeminar(seminar);
        for(User user: seminar.getParticipants()) {
            if(user.getChatId().longValue() != seminar.getChatIdOwner()) {
                bot.sendMessage(user.getChatId(), "Уведомление от семинара *" + seminar.getName() + "*:\n" + message, "Markdown");
            }
        }
    }

    public Collection<ScienceSeminar> getAllSeminars() {
        return primarySeminars.values();
    }

    public void deleteSeminar(ScienceSeminar seminar) {
        if(seminar.getParticipants() != null) {
            seminar.getParticipants().forEach(user -> {
                user.getJoinedSeminars().remove(seminar);
                userManager.saveUser(user);
            });
        }
        primarySeminars.remove(seminar.getId());
        seminarRepository.deleteById(seminar.getId());
        notifyListeners(seminar,false);
    }

    private void notifyListeners(ScienceSeminar seminar, boolean add) {
        listeners.forEach(listener -> {
            listener.update(seminar,add);
        });
    }

    private void notifyListeners(User user, ScienceSeminar seminar) {
        participateListeners.forEach(listener -> {
            listener.participateUser(user,seminar);
        });
    }

    private void initSeminars() {
        seminarRepository.findAll().forEach(seminar -> {
            primarySeminars.put(seminar.getId(),seminar);
        });
    }
}
