package ru.bright.bot.service.managers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bright.bot.model.*;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.seminars.ParticipateUserListener;
import ru.bright.bot.service.seminars.UpdateSeminarsListener;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SeminarsManager {

    private SeminarRepository seminarRepository;
    private List<UpdateSeminarsListener> listeners;
    private List<ParticipateUserListener> participateListeners;
    private SeminarsCacheManager seminarsCacheManager;

    public SeminarsManager(@Autowired SeminarRepository seminarRepository,
                           @Autowired SeminarsCacheManager seminarsCacheManager) {
        this.seminarRepository = seminarRepository;
        this.listeners = new ArrayList<>();
        this.participateListeners = new ArrayList<>();
        this.seminarsCacheManager = seminarsCacheManager;
    }

    public void registerUpdateSeminarsListener(UpdateSeminarsListener listener) {
        listeners.add(listener);
    }

    public void registerParticipateUserListener(ParticipateUserListener listener) {
        participateListeners.add(listener);
    }

    public List<SeminarDTO> getByCategory(String category) {
        return seminarsCacheManager.getAllSeminars().stream().filter(seminar -> (seminar.getSeminarCategoryEnum().equals(category)))
                .collect(Collectors.toList());
    }

    public List<SeminarDTO> getSeminarsByChatIdOwner(Long chatId) {
        return seminarsCacheManager.getAllSeminars().stream().filter(seminar -> (seminar.getChatIdOwner().longValue() == chatId.longValue()))
                .collect(Collectors.toList());
    }

    public Long firstAvailableId() {
        return seminarRepository.findFirstAvailableId();
    }

    public void notify(TelegramBot bot, SeminarDTO seminar, String message) {
        seminar.getMessages().add(message);
        updateSeminar(seminar);
        for(User user: seminar.getParticipants()) {
            if(user.getChatId().longValue() != seminar.getChatIdOwner()) {
                bot.sendMessage(user.getChatId(), "Уведомление от семинара *" + seminar.getName() + "*:\n" + message, "Markdown");
            }
        }
    }

    public void joinTo(User user, long seminarId) {
        seminarsCacheManager.joinTo(user, seminarId);
    }

    public void unjoinFrom(User user, long seminarId) {
        seminarsCacheManager.unjoinFrom(user, seminarId);
    }

    public SeminarDTO updateSeminar(SeminarDTO seminar) {
        return seminarsCacheManager.updateSeminar(seminar);
    }

    public List<SeminarDTO> getAllSeminars() {
        return seminarsCacheManager.getAllSeminars();
    }

    public SeminarDTO findById(long id) {
        return seminarsCacheManager.findById(id);
    }

    public void deleteSeminar(long id) {
        seminarsCacheManager.deleteSeminar(id);
    }

    public List<SeminarDTO> getSeminarsByUserId(long chatId) {
        return seminarsCacheManager.getSeminarsByUserId(chatId);
    }

    public List<ScienceSeminar> getExpiredSeminars(Timestamp timestamp) {
        return seminarRepository.findExpiredSeminarsWithParticipants(timestamp);
    }

}
