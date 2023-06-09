package ru.bright.SpringConferenceBot.model;




import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.sql.Timestamp;
import java.util.*;

@Entity(name="scienceSeminarsTable")
public class ScienceSeminar {

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

   //@Getter
   //@ElementCollection(fetch = FetchType.EAGER)
   //private Set<Long> participantsChatId;


    @ManyToMany(mappedBy = "joinedSeminars", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Getter
    @Setter
    private Set<User> participants = new HashSet<>();

    @Getter
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> messages = new HashSet<>();

    @Getter
    @Setter
    @OneToMany(mappedBy = "seminar", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<UserNotification> notifications = new ArrayList<>();

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;


    public void addMessage(String message) {
        messages.add(message);
    }

    @Override
            public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ScienceSeminar that = (ScienceSeminar) o;

            return id == that.id;
        }

            @Override
            public int hashCode() {
            return (int) (id ^ (id >>> 32));
        }


}
