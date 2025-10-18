package ru.bright.bot.service.utils;

public enum Role {
    ADMIN(3,"Администратор"),
    UNAUTHORIZED_USER(1,"Неавторизованный пользователь"),
    USER(2,"Авторизованный пользователь");

    int priority;
    String name;
    Role(int priority, String name) {
        this.name = name;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return  name;
    }

}
