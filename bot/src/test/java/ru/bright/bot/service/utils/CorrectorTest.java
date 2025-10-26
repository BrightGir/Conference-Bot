package ru.bright.bot.service.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Corrector Unit Tests")
class CorrectorTest {

    @ParameterizedTest
    @DisplayName("Должен правильно склонять слова для разных чисел")
    @CsvSource({
            "1, час, часа, часов, час",
            "2, час, часа, часов, часа",
            "3, час, часа, часов, часа",
            "4, час, часа, часов, часа",
            "5, час, часа, часов, часов",
            "6, час, часа, часов, часов",
            "10, час, часа, часов, часов",
            "11, час, часа, часов, часов",
            "12, час, часа, часов, часов",
            "13, час, часа, часов, часов",
            "14, час, часа, часов, часов",
            "15, час, часа, часов, часов",
            "20, час, часа, часов, часов",
            "21, час, часа, часов, час",
            "22, час, часа, часов, часа",
            "23, час, часа, часов, часа",
            "24, час, часа, часов, часа",
            "25, час, часа, часов, часов",
            "100, час, часа, часов, часов",
            "101, час, часа, часов, час",
            "102, час, часа, часов, часа",
            "111, час, часа, часов, часов",
            "121, час, часа, часов, час"
    })
    void shouldCorrectlyDeclineWords(int number, String var1, String var2, String var3, String expected) {
        // when
        String result = Corrector.plurals(number, var1, var2, var3);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Должен правильно склонять 'день'")
    void shouldDeclineDays() {
        assertThat(Corrector.plurals(1, "день", "дня", "дней")).isEqualTo("день");
        assertThat(Corrector.plurals(2, "день", "дня", "дней")).isEqualTo("дня");
        assertThat(Corrector.plurals(5, "день", "дня", "дней")).isEqualTo("дней");
        assertThat(Corrector.plurals(11, "день", "дня", "дней")).isEqualTo("дней");
        assertThat(Corrector.plurals(21, "день", "дня", "дней")).isEqualTo("день");
    }

    @Test
    @DisplayName("Должен правильно склонять 'участник'")
    void shouldDeclineParticipants() {
        assertThat(Corrector.plurals(1, "участник", "участника", "участников"))
                .isEqualTo("участник");
        assertThat(Corrector.plurals(3, "участник", "участника", "участников"))
                .isEqualTo("участника");
        assertThat(Corrector.plurals(10, "участник", "участника", "участников"))
                .isEqualTo("участников");
    }

    @Test
    @DisplayName("Должен обрабатывать ноль")
    void shouldHandleZero() {
        String result = Corrector.plurals(0, "час", "часа", "часов");
        assertThat(result).isEqualTo("часов");
    }
}

