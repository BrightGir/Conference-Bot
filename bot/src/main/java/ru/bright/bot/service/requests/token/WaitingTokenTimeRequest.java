package ru.bright.bot.service.requests.token;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.requests.BaseRequest;
import ru.bright.bot.service.utils.Role;
import ru.bright.bot.service.utils.Token;
import ru.bright.bot.service.utils.TokenGenerator;

import java.util.Random;

@Slf4j
public class WaitingTokenTimeRequest extends BaseRequest {

    private Role role;
    public WaitingTokenTimeRequest(TelegramBot bot, User user, Role role) {
        super(bot, user);
        this.role = role;
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Введите количество минут, в течение которых будет действовать токен");
    }

    @Override
    public boolean handle(Update update) {
        String message = update.getMessage().getText();
        try {
            int minutes = Integer.parseInt(message.trim());
            Token token = TokenGenerator.generateToken(minutes, role);
            String text = token.getLabel();
            getBot().registerToken(token);
            StringBuilder b = new StringBuilder("Токен успешно создан!\n");
            b.append("Минут действует: " + minutes);
            b.append("\nТекст: " + text);
            getBot().sendMessage(getUser().getChatId(), b.toString());
            return true;
        } catch (NumberFormatException e) {
            getBot().sendMessage(getUser().getChatId(), "Введите корректное число");
            return false;
        }
    }



}
