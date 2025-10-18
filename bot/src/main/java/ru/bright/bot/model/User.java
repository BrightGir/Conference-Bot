package ru.bright.bot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Entity(name = "usersDataTable")
public class User implements Serializable {

    @Id
    @Getter
    @Setter
    private Long chatId;

    @Getter
    @Setter
    private String role;

    @Getter
    @Setter
    private String fio;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return chatId.longValue() == that.chatId.longValue();
    }

    @Override
    public int hashCode() {
        return chatId.hashCode();
    }


}
