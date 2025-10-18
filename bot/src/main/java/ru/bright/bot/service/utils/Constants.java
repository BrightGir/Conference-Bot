package ru.bright.bot.service.utils;

import com.vdurmont.emoji.EmojiParser;

import java.util.Timer;

public class Constants {

    public static String ADMIN_TOKEN = "genius_administration";
    public static String START_COMMAND = "/start";
    public static String GENERATE_TOKEN_COMMAND = "/generatetoken";
    public static String AUTHORIZATION_COMMAND = EmojiParser.parseToUnicode("Авторизация :key:");
    public static String INFO_COMMAND = EmojiParser.parseToUnicode("Информация :page_facing_up:");
    public static String GET_SEMINARS_COMMAND = EmojiParser.parseToUnicode("Семинары :alembic:");
    public static String CREATE_SEMINAR_COMMAND = EmojiParser.parseToUnicode("Создать семинар :abacus:");
    public static String JOINED_SEMINARS = EmojiParser.parseToUnicode("Текущие семинары :bell:");
    public static String EDIT_SEMINAR_COMMAND = EmojiParser.parseToUnicode("Управление семинарами :brick:");
    public static String JOIN_TO_SEMINAR_COMMAND = EmojiParser.parseToUnicode("Присоединиться к семинару :calendar:");
    public static String ADMIN_PANEL = EmojiParser.parseToUnicode("Панель администратора :dizzy:");
    public static Timer TIMER = new Timer();
}