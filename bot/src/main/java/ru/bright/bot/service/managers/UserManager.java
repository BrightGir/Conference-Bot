package ru.bright.bot.service.managers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.bright.bot.model.User;
import ru.bright.bot.model.UserRepository;

@Service
@Slf4j
public class UserManager {


    private UserRepository userRepository;

    public UserManager(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @CachePut(value = "users", key = "#result.chatId")
    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Cacheable(value = "users", key = "#chatId")
    public User getUser(long chatId) {
        return userRepository.findById(chatId).orElse(null);
    }

}
