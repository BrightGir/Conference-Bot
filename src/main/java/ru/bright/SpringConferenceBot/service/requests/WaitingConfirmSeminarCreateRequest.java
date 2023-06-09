package ru.bright.SpringConferenceBot.service.requests;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.SpringConferenceBot.model.ScienceSeminar;
import ru.bright.SpringConferenceBot.model.User;
import ru.bright.SpringConferenceBot.service.TelegramBot;
import ru.bright.SpringConferenceBot.service.callbacks.CallBackDates;
import ru.bright.SpringConferenceBot.service.keyboards.confirm.InlineCreateSeminarConfirmKeyboard;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

public class WaitingConfirmSeminarCreateRequest extends BaseRequest {
    private ScienceSeminar seminar;
    public WaitingConfirmSeminarCreateRequest(TelegramBot bot, User user, ScienceSeminar seminar) {
        super(bot, user);
        this.seminar = seminar;
    }

    @Override
    public void sendCreateHandleMessage() {
        String str = "Так выглядит ваш семинар:\n";
        str += "*" + seminar.getName() + "*";
        str += "\n";
        str += seminar.getLeaderFIO();
        str += "\n";
        LocalDateTime ldt = seminar.getTimestamp().toLocalDateTime();
        String monthString = ldt.getMonth().getDisplayName(TextStyle.FULL,new Locale("ru"));
        str += "_" + ldt.getDayOfMonth() + " " + monthString.substring(0,1).toUpperCase() + monthString.substring(1) + " " + ldt.getYear() + " " +
                ldt.getHour()  + ":" + new DecimalFormat("00").format(ldt.getMinute())  + "_";
        str += "\n";
        str += seminar.getAdditionalInformation();
        str += "\n";
        seminar.setId(Math.abs(ThreadLocalRandom.current().nextLong()));
        getBot().sendMessage(getUser().getChatId(),str,new InlineCreateSeminarConfirmKeyboard(seminar.getId()),"Markdown");

        String finalStr = str;
        getBot().registerCallbackData(CallBackDates.CREATE_SEMINAR_CONFIRM.toString(), (BiConsumer<Update, User>) (update, user) -> {
            long prId = Long.parseLong(update.getCallbackQuery().getData().split("_")[3]);
            if(prId == seminar.getId()) {
                Long newId = getBot().getSeminarsManager().firstAvailableId();
                if(newId == null) {
                    newId = 1L;
                }
                seminar.setId(newId);
            }
            getBot().getSeminarsManager().saveSeminar(seminar);
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText(finalStr);
            editMessageText.setParseMode("Markdown");
            editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
            editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            getBot().executeMethod(editMessageText);
            getBot().sendMessage(user.getChatId(),"Семинар был создан!");
        });
        getBot().registerCallbackData(CallBackDates.CREATE_SEMINAR_DENY.toString(), (BiConsumer<Update, User>) (update, user) -> {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setText("Вы отменили действие");
            editMessageText.setParseMode("Markdown");
            editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
            editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
            getBot().executeMethod(editMessageText);
        });
    }

    @Override
    public boolean handle(Update update) {
        return false;
    }
}
