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


    private UsersCacheManager usersCacheManager;

    public UserManager(@Autowired UsersCacheManager usersCacheManager) {
        this.usersCacheManager = usersCacheManager;
    }

    public User saveUser(User user) {
        return usersCacheManager.saveUser(user);
    }

    public User getUser(long chatId) {
        return usersCacheManager.getUser(chatId);
    }

}
