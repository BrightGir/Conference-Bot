package ru.bright.SpringConferenceBot.service.seminars;

import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;

public interface ParticipateUserListener {

    void participateUser(User user, ScienceSeminar seminar);
}
