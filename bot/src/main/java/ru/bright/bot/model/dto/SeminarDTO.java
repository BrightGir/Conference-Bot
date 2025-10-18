package ru.bright.bot.model.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.model.UserNotification;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SeminarDTO {

    private long id;
    private Long chatIdOwner;
    private Timestamp timestamp;
    private String name;
    public String seminarCategoryEnum;
    private String additionalInformation;
    private String leaderFIO;
    private Set<User> participants = new HashSet<>();
    private List<String> messages = new ArrayList<>();


    public SeminarDTO(ScienceSeminar seminar) {
        this.id = seminar.getId();
        this.chatIdOwner = seminar.getChatIdOwner();
        this.timestamp = seminar.getTimestamp();
        this.name = seminar.getName();
        this.seminarCategoryEnum = seminar.getSeminarCategoryEnum();
        this.additionalInformation = seminar.getAdditionalInformation();
        this.leaderFIO = seminar.getLeaderFIO();
        this.participants = seminar.getParticipants();
        this.messages = seminar.getMessages();
    }

    public static SeminarDTO from(ScienceSeminar seminar) {
        SeminarDTO dto = new SeminarDTO();
        dto.setId(seminar.getId());
        dto.setName(seminar.getName());
        dto.setChatIdOwner(seminar.getChatIdOwner());
        dto.setLeaderFIO(seminar.getLeaderFIO());
        dto.setAdditionalInformation(seminar.getAdditionalInformation());
        dto.setSeminarCategoryEnum(seminar.getSeminarCategoryEnum());
        dto.setTimestamp(seminar.getTimestamp());
        dto.setParticipants(new HashSet<>(seminar.getParticipants()));
        dto.setMessages(new ArrayList<>(seminar.getMessages()));
        return dto;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeminarDTO that = (SeminarDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }




}
