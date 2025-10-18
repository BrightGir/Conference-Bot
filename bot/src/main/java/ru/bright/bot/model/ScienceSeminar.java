package ru.bright.bot.model;




import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity(name="scienceSeminarsTable")
public class ScienceSeminar implements Serializable {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Getter
    @Setter
    private Long chatIdOwner;

    @Getter
    @Setter
    private Timestamp timestamp;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    public String seminarCategoryEnum;

    @Getter
    @Setter
    @Column(length = 1000)
    private String additionalInformation;

    @Getter
    @Setter
    private String leaderFIO;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "seminar_participants",
            joinColumns = @JoinColumn(name = "seminar_id"),
            inverseJoinColumns = @JoinColumn(name = "user_chat_id")
    )
    @Getter
    @Setter
    private Set<User> participants = new HashSet<>();

    //dont use in code
    @OneToMany(mappedBy = "seminar", cascade = CascadeType.REMOVE, orphanRemoval = true,
               fetch = FetchType.LAZY)
    private List<UserNotification> notifications = new ArrayList<>();

    @Getter
    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name = "message_order")
    @Setter
    private List<String> messages = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScienceSeminar that = (ScienceSeminar) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
            return Long.hashCode(id);
        }




}
