package ru.bright.bot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.bright.bot.service.utils.Role;

import java.io.Serializable;
@Entity(name = "user_table")
public class User implements Serializable {

    @Id
    @Getter
    @Setter
    private Long chatId;

    @Getter
    @Setter
    private Role role;

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
