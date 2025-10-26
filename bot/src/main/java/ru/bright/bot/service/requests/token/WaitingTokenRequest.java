package ru.bright.bot.service.requests.token;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.User;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.requests.BaseRequest;
import ru.bright.bot.service.utils.Role;

@Slf4j
public class WaitingTokenRequest extends BaseRequest {
    public WaitingTokenRequest(TelegramBot bot, User user) {
        super(bot, user);
    }

    @Override
    public void sendCreateHandleMessage() {
        getBot().sendMessage(getUser().getChatId(), "Введите токен авторизации");
    }

    @Override
    public boolean handle(Update update) {
        String msg = update.getMessage().getText();
        Role role = getBot().getRoleByToken(msg);

        if(role == null) {
            getBot().sendMessage(update.getMessage().getChatId(),"Токен не найден");
            return true;
        } else {
            if(role.equals(Role.ADMIN)) {
                getUser().setRole(role);
                getBot().getUserManager().saveUser(getUser());
                getBot().sendMessage(update.getMessage().getChatId(),"Вы успешно авторизовались!");
                return true;
            }
            if(!getBot().getTokenByLabel(msg).isActive()) {
                getBot().sendMessage(update.getMessage().getChatId(),"Токен недействителен");
                return true;
            } else {
                getUser().setRole(role);
                getBot().getUserManager().saveUser(getUser());
                getBot().sendMessage(update.getMessage().getChatId(),"Вы успешно авторизовались!");
            }
        }
        return true;
    }
}
