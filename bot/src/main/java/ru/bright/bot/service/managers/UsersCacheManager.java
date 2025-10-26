package ru.bright.bot.service.managers;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.bright.bot.model.User;
import ru.bright.bot.model.UserRepository;

@Service
public class UsersCacheManager {

    private final UserRepository userRepository;

    public UsersCacheManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @CachePut(value = "users", key = "#result.chatId", unless = "#result == null")
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Cacheable(value = "users", key = "#chatId", unless = "#result == null")
    public User getUser(long chatId) {
        return userRepository.findById(chatId).orElse(null);
    }
}
