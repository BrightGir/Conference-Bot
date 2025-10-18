package ru.bright.bot.service.seminars;

import lombok.Getter;

public enum SeminarCategory {
    MATH("Математика"),
    PHYSICS("Физика"),
    OTHER("Другое");

    @Getter
    String name;
    SeminarCategory(String name) {
        this.name = name;
    }


}
