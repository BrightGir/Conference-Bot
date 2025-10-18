package ru.bright.bot.service.utils;

public class Corrector {

    public static String plurals(int number, String var1, String var2, String var3) {
        String correctString = "ошибка";
        int number1 = number%10;
        int number2 = number%100;
        if(number1 == 1) {
            correctString = var1;
        }
        if(number1 >= 2 && number1 <= 4) {
            correctString = var2;
        }
        if((number1 >= 5) && (number1 <= 9) || (number1 == 0) || (number2 >= 11) && (number2 <= 14)) {
            correctString = var3;
        }
        return correctString;
    }
}
