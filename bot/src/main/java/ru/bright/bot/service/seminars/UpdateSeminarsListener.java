package ru.bright.bot.service.seminars;

import ru.bright.bot.model.ScienceSeminar;

public interface UpdateSeminarsListener {

    void update(ScienceSeminar seminar, boolean add);
}
