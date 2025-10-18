package ru.bright.bot.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodSerializable;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bright.bot.config.BotConfig;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.callbacks.*;
import ru.bright.bot.service.callbacks.page.JoinedSeminarsPageCallbackAction;
import ru.bright.bot.service.callbacks.page.OwnSeminarAdditionalInfoPageCallbackAction;
import ru.bright.bot.service.callbacks.page.SeminarsPageByCategoryCallbackAction;
import ru.bright.bot.service.callbacks.page.SeminarsPageByChatIdCallbackAction;
import ru.bright.bot.service.commands.*;
import ru.bright.bot.service.commands.admin.AdminPanelCommand;
import ru.bright.bot.service.keyboards.reply.AdminKeyboard;
import ru.bright.bot.service.keyboards.reply.BaseReplyKeyboard;
import ru.bright.bot.service.keyboards.reply.FunctionalReplyKeyBoard;
import ru.bright.bot.service.keyboards.InlineSeminarPanelKeyboard;
import ru.bright.bot.service.managers.UserManager;
import ru.bright.bot.service.managers.UserNotificationManager;
import ru.bright.bot.service.requests.BaseRequest;
import ru.bright.bot.service.requests.WaitingNotifyMessageRequest;
import ru.bright.bot.service.requests.WaitingSeminarIdToDeleteRequest;
import ru.bright.bot.service.requests.WaitingSeminarPanelIdRequest;
import ru.bright.bot.service.seminars.SeminarCategory;
import ru.bright.bot.service.managers.SeminarsManager;
import ru.bright.bot.service.utils.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

@Component
@Slf4j
@Scope("singleton")
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    @Getter
    private UpdatesHandler updatesHandler;

    @Autowired
    private BaseReplyKeyboard baseReplyKeyboard;

    @Autowired
    private FunctionalReplyKeyBoard functionalReplyKeyBoard;

    @Autowired
    private AdminKeyboard adminKeyboard;
    private Map<Long, BaseRequest> requests;
    private Map<Long, TimerTask> requestsTimers;

    private Map<String, Token> tokens;
    private Timer timer = Constants.TIMER;
    private Map<String, BiConsumer<Update,User>> callbackConsumerData;
    private Map<String, CallbackAction> callbackData;
    private final Map<String, String> lastMessageText = new ConcurrentHashMap<>();

    @Getter
    @Autowired
    private SeminarsManager seminarsManager;

    @Getter
    @Autowired
    private UserNotificationManager userNotificationManager;

    @Getter
    @Autowired
    private UserManager userManager;



    public TelegramBot(BotConfig config ) {
        this.config = config;
        this.updatesHandler = new UpdatesHandler(this);
        this.tokens = new HashMap<>();
        this.requests = new HashMap<>();
        this.requestsTimers = new HashMap<>();
        this.callbackConsumerData = new HashMap<>();
        this.callbackData = new HashMap<>();
      //  this.seminarsManager = new SeminarsManager(seminarRepository);
        registerCommands();
        registerAdminToken();
        try {
            registerCallbackActions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerAdminToken() {
        Token token = new Token(Integer.MAX_VALUE, Role.ADMIN.toString(), config.getAdminToken());
        registerToken(token);
    }



    private void registerCallbackActions() {

        for(SeminarCategory c: SeminarCategory.values()) {
            this.registerCallbackData(c.toString(),new CategoryCallbackAction(c.toString(),this));
            SeminarsPageByCategoryCallbackAction action = new SeminarsPageByCategoryCallbackAction(this,c.toString());
            this.registerCallbackData(CallBackDates.SEMINARS.toString() + "_" + c.toString(),action);
        }
        this.registerCallbackData(CallBackDates.OWNSEMINARS.toString(),new SeminarsPageByChatIdCallbackAction(this));
        this.registerCallbackData(CallBackDates.BACK_TO_CATEGORIES.toString(),new BackToCategoriesCallbackAction(this));
        this.registerCallbackData(CallBackDates.BACK_TO_PANEL.toString(), (BiConsumer<Update, User>) (update, user) ->  {
            ScienceSeminar seminar = this.getSeminarsManager().findById(Long.parseLong(update.getCallbackQuery().getData().split("_")[3]));
            String s = "Семинар *" + seminar.getName() + "*. ";
            editMessage(update.getCallbackQuery(),s + "Выберите действие:",new InlineSeminarPanelKeyboard(seminar.getId()),"Markdown");
        });
        this.registerCallbackData(CallBackDates.EDIT_TIME.toString(),new EditDateCallbackAction(this));
        this.registerCallbackData(CallBackDates.EDIT_SEMINAR.toString(),new EditSeminarCallbackAction(this));
        this.registerCallbackData(CallBackDates.JOINED_SEMINARS.toString(),new JoinedSeminarsPageCallbackAction(this));
        this.registerCallbackData(CallBackDates.LEAVE_SEMINAR.toString(),new LeaveSeminarCallbackAction(this));
        this.registerCallbackData(CallBackDates.SET_FIO.toString(),new SetFIOCallbackAction(this));
        this.registerCallbackData(CallBackDates.OWN_SEMINARS_ADDITIONAL_INFORMATION.toString(),new OwnSeminarAdditionalInfoCallbackAction(this));
        this.registerCallbackData(CallBackDates.OWN_SEMINARS_ADDITIONAL_INFORMATION_PAGE.toString(),new OwnSeminarAdditionalInfoPageCallbackAction(this));
        this.registerCallbackData(CallBackDates.DELETE_CURRENT_SEMINAR.toString(),new DeleteSeminarCallbackAction(this));
        this.registerCallbackData(CallBackDates.MANAGE_SEMINAR.toString(),new SeminarManageCallbackAction(this));
        this.registerCallbackData(CallBackDates.SEMINAR_NOTIFIES.toString(),new SeminarNotifiesCallbackAction(this));
        this.registerCallbackData(CallBackDates.TIME_NOTIFY.toString(),new TimeNotifyCallbackAction(this));
        this.registerCallbackData(CallBackDates.SEMINAR_TIME_NOTIFIES.toString(),new SeminarTimeNotifiesCallbackAction(this));
        this.registerCallbackData(CallBackDates.NOTIFY_PARTICIPANTS.toString(), (BiConsumer<Update, User>) (update, user) -> {
            long id = Long.parseLong(update.getCallbackQuery().getData().split("_")[2]);
            this.sendRequest(new WaitingNotifyMessageRequest(this,user,seminarsManager.findById(id)),5);
        });
        this.registerCallbackData(CallBackDates.SEMINAR_PANEL.toString(), (BiConsumer<Update, User>) (update, user) -> {
            this.sendRequest(new WaitingSeminarPanelIdRequest(this,user),5);
        });
        this.registerCallbackData(CallBackDates.DELETE_SEMINAR.toString(), (BiConsumer<Update, User>) (upd, user) -> {
            this.sendRequest(new WaitingSeminarIdToDeleteRequest(this,user),5);
        });
        this.registerCallbackData(CallBackDates.DELETE_SEMINAR_ACCEPT.toString(), (BiConsumer<Update, User>) (upd, user) -> {
            long seminarId = Long.parseLong(upd.getCallbackQuery().getData().split("_")[3]);
            ScienceSeminar sem = this.getSeminarsManager().findById(seminarId);
            this.getSeminarsManager().deleteSeminar(sem);
            editMessage(upd.getCallbackQuery(),"Семинар *" + sem.getName() + "* был удален", "Markdown");
        });
        this.registerCallbackData(CallBackDates.DELETE_SEMINAR_DENY.toString(), (BiConsumer<Update, User>) (upd, user) -> {
            editMessage(upd.getCallbackQuery(),"Вы отменили действие", "Markdown");
        });

        this.registerCallbackData(CallBackDates.LEAVE_SEMINAR_ACCEPT.toString(), (BiConsumer<Update, User>) (upd, user) -> {
            long seminarId = Long.parseLong(upd.getCallbackQuery().getData().split("_")[3]);
            ScienceSeminar sem = this.getSeminarsManager().findById(seminarId);
            getSeminarsManager().unjoinFrom(user,sem);
            editMessage(upd.getCallbackQuery(),"Вы успешно покинули семинар *" + sem.getName() + "*", "Markdown");
        });
        this.registerCallbackData(CallBackDates.LEAVE_SEMINAR_DENY.toString(), (BiConsumer<Update, User>) (upd, user) -> {
            editMessage(upd.getCallbackQuery(),"Вы отменили действие");
        });

    }

    public void editMessage(CallbackQuery query, String text, InlineKeyboardMarkup keyboard, String parseMod) {
        int messageId = query.getMessage().getMessageId();
        long chatId = query.getMessage().getChatId();
        try {

            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setMessageId(messageId);
            editMessageText.setText(text);
            editMessageText.setParseMode(parseMod);
            editMessageText.setChatId(chatId);
            editMessageText.setReplyMarkup(keyboard);
            this.executeMethod(editMessageText);
        } catch (RuntimeException ex) {

        }
    }

    public void editMessage(CallbackQuery query, String text, InlineKeyboardMarkup keyboard) {
        try {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(text);
        editMessageText.setChatId(query.getMessage().getChatId());
        editMessageText.setReplyMarkup(keyboard);
        editMessageText.setMessageId(query.getMessage().getMessageId());
        this.executeMethod(editMessageText);
        } catch (RuntimeException ex) {

        }
    }

    public void editMessage(CallbackQuery query, String text, String parseMod) {
        try {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(text);
        editMessageText.setParseMode(parseMod);
        editMessageText.setChatId(query.getMessage().getChatId());
        editMessageText.setMessageId(query.getMessage().getMessageId());
        this.executeMethod(editMessageText);
        } catch (RuntimeException ex) {

        }
    }

    public void editMessage(CallbackQuery query, String text) {
        try {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(text);
        editMessageText.setChatId(query.getMessage().getChatId());
        editMessageText.setMessageId(query.getMessage().getMessageId());
        this.executeMethod(editMessageText);
        } catch (RuntimeException ex) {

        }
    }

    private void registerCommands() {
        updatesHandler.registerCommand(new StartCommand(this));
        updatesHandler.registerCommand(new AuthorizationCommand(this));
        updatesHandler.registerCommand(new GenerateTokenCommand(this));
        updatesHandler.registerCommand(new InformationCommand(this));
        updatesHandler.registerCommand(new CreateSeminarCommand(this));
        updatesHandler.registerCommand(new GetSeminarsCommand(this));
        updatesHandler.registerCommand(new EditSeminarCommand(this));
        updatesHandler.registerCommand(new JoinToSeminarCommand(this));
        updatesHandler.registerCommand(new JoinedSeminarsCommand(this));
        updatesHandler.registerCommand(new AdminPanelCommand(this));
        //updatesHandler.registerCommand(Constants.AUTHORIZATION_COMMAND,new StartCommand(this));
    }


    public void registerCallbackData(String callBackData, BiConsumer<Update,User> action) {
        callbackConsumerData.put(callBackData,action);
    }

     public void registerCallbackData(String callBackData, CallbackAction action) {
        callbackData.put(callBackData,action);
    }

    public Map<String,BiConsumer<Update,User>> getCallbackConsumerData() {
        return callbackConsumerData;
    }

    public Map<String, CallbackAction> getCallbackData() {
        return callbackData;
    }

    public void registerToken(Token token) {
        this.tokens.put(token.getLabel(),token);
    }

    public Map<String,Token> getTokens() {
        return tokens;
    }

    public void sendRequest(BaseRequest request, int minutesToExpire) {
        Long chatId = request.getUser().getChatId();
        if(requestsTimers.containsKey(chatId)) {
            requestsTimers.get(chatId).cancel();
        }
        requests.put(chatId,request);
        request.sendCreateHandleMessage();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(requests.containsKey(chatId)) {
                    requests.get(chatId).setCancelled(true);
                }
            }
        };
        timer.schedule(task,1000L * 60L * minutesToExpire);
        requestsTimers.put(chatId,task);
    }

    public String getRoleByToken(String token) {
        if(token.equals(Constants.ADMIN_TOKEN)) {
            return Role.ADMIN.toString();
        }
        if(tokens.get(token) == null) {
            return null;
        }
        return tokens.get(token).getRole();
    }

    public Token getTokenByLabel(String name) {
        return tokens.get(name);
    }

    public Map<Long,TimerTask> getRequestsTimers() {
        return requestsTimers;
    }
    public Map<Long,BaseRequest> getRequests() {
        return requests;
    }


    @Override
    public void onUpdateReceived(Update update) {
        updatesHandler.handle(update);
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getKey();
    }

    public void sendMessage(Long chatId,String text) {
        try {
            execute(prepareToSend(chatId,text));
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить сообщение " + e.getMessage());
        }
    }

    private SendMessage prepareToSend(Long chatId, String text) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(chatId);
        sendMsg.setText(text);
        Optional<User> optUser = Optional.ofNullable(getUserManager().getUser(chatId));
        if(optUser.isEmpty()) {
            ReplyKeyboardMarkup k = new ReplyKeyboardMarkup();
            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton();
            button.setText("/start");
            row.add(button);
            k.setKeyboard(Arrays.asList(row));
            sendMsg.setReplyMarkup(k);
        } else {
            if (!optUser.get().getRole().equals(Role.UNAUTHORIZED_USER.toString())) {
                if(optUser.get().getRole().equals(Role.ADMIN.toString())) {
                    sendMsg.setReplyMarkup(adminKeyboard);
                } else {
                    sendMsg.setReplyMarkup(functionalReplyKeyBoard);
                }

            } else {
                sendMsg.setReplyMarkup(baseReplyKeyboard);

            }
        }
        return sendMsg;
    }

    public void sendMessage(Long chatId, String text, InlineKeyboardMarkup inlineKeyboard) {
        SendMessage sendMsg = prepareToSend(chatId,text);
        sendMsg.setReplyMarkup(inlineKeyboard);
        try {
            execute(sendMsg);
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить сообщение " + e.getMessage());
        }
    }


    public void sendMessage(Long chatId, String text, InlineKeyboardMarkup inlineKeyboard, String parseMode) {
        SendMessage sendMsg = prepareToSend(chatId,text);
        sendMsg.setReplyMarkup(inlineKeyboard);
        sendMsg.setParseMode(parseMode);
        try {
            execute(sendMsg);
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить сообщение " + e.getMessage());
        }
    }

    public void sendMessage(Long chatId, String text, String parseMode) {
        SendMessage sendMsg = prepareToSend(chatId,text);
        sendMsg.setParseMode(parseMode);
        try {
            execute(sendMsg);
        } catch (Exception e) {
            log.error("Не удалось отправить сообщение " + e.getMessage());
        }
    }

    public void executeMethod(BotApiMethodSerializable method) {
        try {
            execute(method);
        } catch (Exception e) {
            log.error("Не удалось выполнить метод " + method.getMethod());
          //  e.printStackTrace();
        }
    }
}
