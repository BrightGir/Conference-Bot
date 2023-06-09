package ru.bright.SpringConferenceBot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity(name = "usersDataTable")
public class User {

    @Id
    @Getter
    @Setter
    private Long chatId;

    @Getter
    @Setter
    private String role;

    @Getter
    @Setter
    private String FIO;

 //  @ElementCollection(fetch = FetchType.EAGER)
 //  @Getter
 //  @Setter
 //  private Set<Long> joinedSeminars = new HashSet<>();

    @Getter
    @Setter
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_seminar",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="seminar_id"))
    private Set<ScienceSeminar> joinedSeminars = new HashSet<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return chatId.longValue() == that.chatId.longValue();
    }

    @Override
    public int hashCode() {
        return (int) (chatId ^ (chatId >>> 32));
    }


}
