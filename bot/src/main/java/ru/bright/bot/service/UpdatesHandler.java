package ru.bright.bot.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.bright.bot.model.User;
import ru.bright.bot.service.commands.AuthorizationCommand;
import ru.bright.bot.service.commands.Command;
import ru.bright.bot.service.commands.StartCommand;
import ru.bright.bot.service.requests.BaseRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class UpdatesHandler {


    @Getter
    private TelegramBot bot;
    private Map<String, Command> commands;

    public UpdatesHandler(TelegramBot bot) {
        this.bot = bot;
        this.commands = new HashMap<>();
    }

    public void registerCommand(Command cmd, String... aliases) {
        for(String label: cmd.getLabels()) {
            commands.put(label,cmd);
        }
        for(String s: aliases) {
            commands.put(s,cmd);
        }
    }

    public void handle(Update update) {
        if(!update.hasMessage() && !update.hasCallbackQuery()) return;
        if(update.hasMessage()) {
            String msg = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            Optional<User> opt = Optional.ofNullable(bot.getUserManager().getUser(chatId));
            if (opt.isEmpty() && !msg.equals("/start")) {
                bot.sendMessage(update.getMessage().getChatId(), "Для начала работы с ботом пропишите команду /start");
                return;
            }
            // if(opt.isEmpty()) {
                //     bot.sendMessage(chatId,"Вы не авторизованы!");
                //     return;
                // }
            if (msg.charAt(0) == '/') {
                if (!commands.containsKey(msg)) {
                    bot.sendMessage(update.getMessage().getChatId(), "Неизвестная команда");
                    return;
                }
            }
            if (commands.containsKey(msg)) {
                if(opt.isPresent() || (opt.isEmpty() && (commands.get(msg) instanceof AuthorizationCommand || commands.get(msg) instanceof StartCommand))) {
                    commands.get(msg).execute(update);
                }
                return;
            }
            if (bot.getRequests().containsKey(chatId)) {
                BaseRequest req = getBot().getRequests().get(chatId);
                if (req.isCancelled()) {
                    bot.sendMessage(chatId, "Запрос недействителен");
                    bot.getRequests().remove(chatId);
                } else {
                    if (req.handle(update)) {
                        bot.getRequests().remove(chatId);
                    }
                }
            }
        }
        if(update.hasCallbackQuery()) {
            String query = update.getCallbackQuery().getData();
            String qwp = getQueryWithoutParam(query);
            if(!bot.getCallbackConsumerData().containsKey(qwp) && !bot.getCallbackData().containsKey(qwp)) {
                log.error("Запрос " + query + " не обработан");
                return;
            }
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Optional<User> opt = Optional.ofNullable(bot.getUserManager().getUser(chatId));
            if(bot.getCallbackData().containsKey(qwp)) {
                bot.getCallbackData().get(qwp).action(update,opt.get());
            }
            if(bot.getCallbackConsumerData().containsKey(qwp)) {
                bot.getCallbackConsumerData().get(qwp).accept(update,opt.get());
            }

        }

    }


    private String getQueryWithoutParam(String query) {
        String newQuery = "";
        String[] m = query.split("_");
        if(m.length == 1) {
            return query;
        }
        newQuery = m[0] + "_";
        for(int i = 1; i < m.length; i++) {
            try {
                Long.parseLong(m[i]);
            } catch (NumberFormatException e) {
                newQuery += m[i] + "_";
            }
        }
        return newQuery.substring(0,newQuery.length()-1);
    }




}
