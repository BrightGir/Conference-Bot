package ru.bright.SpringConferenceBot.service.requests.token;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.requests.BaseRequest;
import ru.bright.SpringConferenceBot.service.utils.Token;

import java.util.Random;

@Slf4j
public class WaitingTokenTimeRequest extends BaseRequest {

    private String role;
    public WaitingTokenTimeRequest(TelegramBot bot, User user, String role) {
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
            String text = generateRandomString(15);
            getBot().registerToken(new Token(minutes,role,text));
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

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

}
