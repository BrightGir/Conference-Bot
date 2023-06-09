package ru.bright.SpringConferenceBot.service.requests;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.requests.token.WaitingTokenTimeRequest;
import ru.bright.SpringConferenceBot.service.utils.Role;
import ru.bright.SpringConferenceBot.service.TelegramBot;

public class WaitingRoleRequest extends BaseRequest{
    public WaitingRoleRequest(TelegramBot bot, User user) {
        super(bot, user);
    }

    @Override
    public void sendCreateHandleMessage() {
        StringBuilder b = new StringBuilder("Введите группу пользователя, для которого вы хотите создать токен");
        b.append("\nДоступные группы:");
        int counter = 1;
        b.append("\n");
        for(Role role: Role.values()) {
            if(role != Role.ADMIN && role != Role.UNAUTHORIZED_USER) {
                b.append("\n");
                b.append(counter + ". " + role.toString() + " (" + role.getName() + ")");
                counter++;
            }
        }
        getBot().sendMessage(getUser().getChatId(),b.toString());
    }

    @Override
    public boolean handle(Update update) {
        String message = update.getMessage().getText();
        message = message.trim();
        try {
            Role role = Role.valueOf(message);
            getBot().sendRequest(new WaitingTokenTimeRequest(getBot(),getUser(),role.toString()),3);
            return false;
        } catch (IllegalArgumentException e) {
            StringBuilder b = new StringBuilder("Введите корректную группу пользователя");
            b.append("\nДоступные группы:\n");
            int counter = 1;
            for(Role role: Role.values()) {
                if(role != Role.ADMIN && role != Role.UNAUTHORIZED_USER) {
                    b.append("\n");
                    b.append(counter + ". " + role.toString() + " (" + role.getName() + ")");
                    counter++;
                }
            }
            getBot().sendMessage(update.getMessage().getChatId(),b.toString());
            return false;
        }
    }
}
