package ru.bright.SpringConferenceBot.service.seminars;

import ru.bright.SpringConferenceBot.model.ScienceSeminar;

public interface UpdateSeminarsListener {

    void update(ScienceSeminar seminar, boolean add);
}
