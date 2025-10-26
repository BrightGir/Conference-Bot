package ru.bright.bot.service.utils;

import lombok.Getter;

import java.util.Timer;
import java.util.TimerTask;

public class Token {

    @Getter
    private String label;

    @Getter
    private boolean active;
    @Getter
    private Role role;
    private Timer timer = Constants.TIMER;

    public Token(int minutesToEnd, Role role, String label) {
        this.active = true;
        this.role = role;
        this.label = label;
        startDestructionIn(minutesToEnd);
    }

    public void destroy() {
        active = false;
    }

    private void startDestructionIn(int minutes) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                active = false;
            }
        },1000L * 60L * minutes);
    }
}
