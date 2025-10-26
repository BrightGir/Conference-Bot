package ru.bright.bot.service.requests;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.callbacks.CallBackDates;
import ru.bright.bot.service.keyboards.confirm.InlineCreateSeminarConfirmKeyboard;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

public class WaitingConfirmSeminarCreateRequest extends BaseRequest {
    private SeminarDTO seminar;

    public WaitingConfirmSeminarCreateRequest(TelegramBot bot, User user, SeminarDTO seminar) {
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
        ZonedDateTime zdt = seminar.getTimestamp().toInstant()
                .atZone(ZoneId.of("Europe/Moscow"));
        String monthString = zdt.getMonth().getDisplayName(TextStyle.FULL,new Locale("ru"));
        str += "_" + zdt.getDayOfMonth() + " " + monthString.substring(0,1).toUpperCase() + monthString.substring(1) + " " + zdt.getYear() + " " +
                zdt.getHour()  + ":" + new DecimalFormat("00").format(zdt.getMinute())  + "_";
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
            getBot().getSeminarsManager().updateSeminar(seminar);
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
