package ru.bright.SpringConferenceBot.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.model.UserRepository;

import java.util.HashMap;

@Component
@Scope("singleton")
@Slf4j
public class UserManager {


    private UserRepository userRepository;
    private HashMap<Long, User> users;

    public UserManager(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
        this.users = new HashMap<>();
        initUsers();
    }

    private void initUsers() {
        userRepository.findAll().forEach(user -> {
            users.put(user.getChatId(),user);
        });
    }

    public void saveUser(User user) {
    //    log.info("save user, id = " + user.getChatId());
        users.put(user.getChatId(),user);
        userRepository.save(user);
    }

    public User getUser(long chatId) {
        return users.get(chatId);
    }
}
