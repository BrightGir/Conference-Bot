package ru.bright.bot.service.managers;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.util.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private CacheManager cacheManager;

    public SeminarsManager(@Autowired SeminarRepository seminarRepository, @Autowired CacheManager cacheManager) {
        this.seminarRepository = seminarRepository;
        this.listeners = new ArrayList<>();
        this.participateListeners = new ArrayList<>();
        this.cacheManager = cacheManager;
    }

    public void registerUpdateSeminarsListener(UpdateSeminarsListener listener) {
        listeners.add(listener);
    }

    public void registerParticipateUserListener(ParticipateUserListener listener) {
        participateListeners.add(listener);
    }

    public List<SeminarDTO> getByCategory(String category) {
        return getAllSeminars().stream().filter(seminar -> (seminar.getSeminarCategoryEnum().equals(category)))
                .collect(Collectors.toList());
    }

    public List<SeminarDTO> getSeminarsByChatIdOwner(Long chatId) {
        return getAllSeminars().stream().filter(seminar -> (seminar.getChatIdOwner().longValue() == chatId.longValue()))
                .collect(Collectors.toList());
    }

    public Long firstAvailableId() {
        return seminarRepository.findFirstAvailableId();
    }

    public void notify(TelegramBot bot, ScienceSeminar seminar, String message) {
        seminar.getMessages().add(message);
        saveSeminar(seminar);
        for(User user: seminar.getParticipants()) {
            //System.out.println(String.format("Participant %s: %s", user.getChatId(), user.getFIO()));
            if(user.getChatId().longValue() != seminar.getChatIdOwner()) {
                bot.sendMessage(user.getChatId(), "Уведомление от семинара *" + seminar.getName() + "*:\n" + message, "Markdown");
            }
        }
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

    public List<ScienceSeminar> getExpiredSeminars(Timestamp timestamp) {
        return seminarRepository.findExpiredSeminars(timestamp);
    }


    @CacheEvict(value = "user_seminars", key = "#user.chatId")
    public void joinTo(User user, ScienceSeminar seminar) {
        seminar.getParticipants().add(user);
        this.saveSeminar(seminar);
        notifyListeners(user,seminar);
    }

    @CacheEvict(value = "user_seminars", key = "#user.chatId")
    public void unjoinFrom(User user, ScienceSeminar seminar) {
        seminar.getParticipants().remove(user);
        this.saveSeminar(seminar);
        notifyListeners(user,seminar);
    }

    @CachePut(value = { "seminars" }, key = "#result.id")
    public SeminarDTO saveSeminar(ScienceSeminar seminar) {
        ScienceSeminar savedSeminar = seminarRepository.save(seminar);
        for (User participant : seminar.getParticipants()) {
            cacheManager.getCache("user_seminars").evict(participant.getChatId());
        }
        SeminarDTO dto = SeminarDTO.from(savedSeminar);
        notifyListeners(savedSeminar,true);
        return dto;
    }

    @Cacheable(value = "allSeminars")
    public List<SeminarDTO> getAllSeminars() {
        List<SeminarDTO> seminars = new ArrayList<>();
        // Используем метод с JOIN FETCH вместо findAll() для избежания N+1
        seminarRepository.findAllWithParticipants().forEach(s -> {
            seminars.add(SeminarDTO.from(s));
        });
        return seminars;
    }

    @Cacheable(value = "seminars", key = "#id")
    public ScienceSeminar findById(long id) {
        ScienceSeminar seminar = seminarRepository.findByIdWithParticipants(id).orElse(null);
        if (seminar != null) {
            seminar.setParticipants(new HashSet<>(seminar.getParticipants()));
            seminar.setMessages(new ArrayList<>(seminar.getMessages()));
        }
        return seminar;
    }

    @CacheEvict(value = { "seminars" }, key = "#seminar.id")
    public void deleteSeminar(ScienceSeminar seminar) {
        seminarRepository.deleteById(seminar.getId());
        for (User participant : seminar.getParticipants()) {
            cacheManager.getCache("user_seminars").evict(participant.getChatId());
        }
        notifyListeners(seminar,false);
    }

    @Cacheable(value = "user_seminars", key = "#chatId")
    public List<SeminarDTO> getSeminarsByUserId(long chatId) {
        List<SeminarDTO> seminarDTOS = new ArrayList<>();
        seminarRepository.findByUserId(chatId).forEach(s -> {
            seminarDTOS.add(SeminarDTO.from(s));
        });
        return seminarDTOS;
    }

}
