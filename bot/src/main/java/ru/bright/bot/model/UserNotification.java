package ru.bright.bot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
public class UserNotification implements Serializable {

    @Getter
    @Setter
    private boolean isNotified;

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seminar_id", referencedColumnName = "id")
    @Getter
    @Setter
    private ScienceSeminar seminar;

    @ManyToOne
    @JoinColumn(name="user_chat_id", referencedColumnName = "chatId")
    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    private boolean isActive;

    @Getter
    @Setter
    private int hour;


}
