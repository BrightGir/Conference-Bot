package ru.bright.bot.service.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.SeminarRepository;
import ru.bright.bot.model.User;
import ru.bright.bot.model.dto.SeminarDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SeminarsCacheManager {

    private final CacheManager cacheManager;
    private SeminarRepository seminarRepository;

    public SeminarsCacheManager(@Autowired CacheManager cacheManager,
                                @Autowired SeminarRepository seminarRepository) {
        this.cacheManager = cacheManager;
        this.seminarRepository = seminarRepository;
    }

    @Transactional
    @CacheEvict(value = "user_seminars", key = "#user.chatId")
    public void joinTo(User user, long seminarId) {
        ScienceSeminar seminar = seminarRepository.findByIdWithParticipantsAndMessages(seminarId).orElse(null);
        if(seminar != null) {
            seminar.getParticipants().add(user);
            seminarRepository.save(seminar);
        }
    }

    @Transactional
    @CacheEvict(value = "user_seminars", key = "#user.chatId")
    public void unjoinFrom(User user, long seminarId) {
        ScienceSeminar seminar = seminarRepository.findByIdWithParticipantsAndMessages(seminarId).orElse(null);
        if(seminar != null) {
            seminar.getParticipants().remove(user);
            seminarRepository.save(seminar);
        }
    }

    @Transactional
    @CachePut(value = { "seminars" }, key = "#result.id", unless = "#result == null")
    @CacheEvict(value = "allSeminars", allEntries = true)
    public SeminarDTO updateSeminar(SeminarDTO seminar) {
        ScienceSeminar seminarEntity = seminarRepository.findById(seminar.getId()).orElse(null);
        if(seminarEntity == null) {
            seminarEntity = new ScienceSeminar();
            seminarEntity.setId(seminar.getId());
        }
        if(seminar.getName() != null) {
            seminarEntity.setName(seminar.getName());
        }
        if(seminar.getSeminarCategoryEnum() != null) {
            seminarEntity.setSeminarCategoryEnum(seminar.getSeminarCategoryEnum());
        }
        if(seminar.getTimestamp() != null) {
            seminarEntity.setTimestamp(seminar.getTimestamp());
        }
        if(seminar.getLeaderFIO() != null) {
            seminarEntity.setLeaderFIO(seminar.getLeaderFIO());
        }
        if(seminar.getAdditionalInformation() != null) {
            seminarEntity.setAdditionalInformation(seminar.getAdditionalInformation());
        }
        if(seminar.getChatIdOwner() != null) {
            seminarEntity.setChatIdOwner(seminar.getChatIdOwner());
        }
        if(seminar.getMessages() != null) {
            seminarEntity.setMessages(seminar.getMessages());
        }

        ScienceSeminar savedSeminar = seminarRepository.save(seminarEntity);
        for (User participant : savedSeminar.getParticipants()) {
            cacheManager.getCache("user_seminars").evict(participant.getChatId());
        }
        return SeminarDTO.from(savedSeminar);
    }

    @Cacheable(value = "allSeminars")
    public List<SeminarDTO> getAllSeminars() {
        List<SeminarDTO> seminars = new ArrayList<>();
        seminarRepository.findAllWithParticipantsAndMessages().forEach(s -> {
            seminars.add(SeminarDTO.from(s));
        });
        return seminars;
    }

    @Cacheable(value = "seminars", key = "#id", unless = "#result == null")
    public SeminarDTO findById(long id) {
        ScienceSeminar seminar = seminarRepository.findByIdWithParticipantsAndMessages(id).orElse(null);
        if(seminar == null) return null;
        return SeminarDTO.from(seminar);
    }


    @Transactional
    @CacheEvict(value = { "seminars", "allSeminars" }, allEntries = true)
    public void deleteSeminar(long id) {
        ScienceSeminar seminar = seminarRepository.findByIdWithParticipantsAndMessages(id).orElse(null);
        if(seminar == null) return;
        Set<User> participants = new HashSet<>(seminar.getParticipants());
        seminarRepository.deleteById(seminar.getId());
        for (User participant : participants) {
            cacheManager.getCache("user_seminars").evict(participant.getChatId());
        }
    }

    @Cacheable(value = "user_seminars", key = "#chatId")
    public List<SeminarDTO> getSeminarsByUserId(long chatId) {
        List<SeminarDTO> seminarDTOS = new ArrayList<>();
        seminarRepository.findByUserIdWithParticipantsAndMessages(chatId).forEach(s -> {
            seminarDTOS.add(SeminarDTO.from(s));
        });
        return seminarDTOS;
    }
}
