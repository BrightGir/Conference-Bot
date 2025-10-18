package ru.bright.bot.service.seminars;

import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;

public interface ParticipateUserListener {

    void participateUser(User user, ScienceSeminar seminar);
}
