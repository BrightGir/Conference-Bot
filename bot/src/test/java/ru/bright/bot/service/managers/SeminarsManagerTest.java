package ru.bright.bot.service.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.SeminarRepository;
import ru.bright.bot.model.User;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.TelegramBot;
import ru.bright.bot.service.utils.Role;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeminarsManagerTest {

    @Mock
    private SeminarRepository seminarRepository;

    @Mock
    private SeminarsCacheManager seminarsCacheManager;

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private SeminarsManager seminarsManager;

    private SeminarDTO testSeminar;
    private User testUser;
    private User ownerUser;

    @BeforeEach
    void setUp() {
        ownerUser = new User();
        ownerUser.setChatId(111111L);
        ownerUser.setFio("Michael Petrov");
        ownerUser.setRole(Role.USER);

        testUser = new User();
        testUser.setChatId(123456L);
        testUser.setFio("Anton Merik");
        testUser.setRole(Role.USER);

        testSeminar = new SeminarDTO();
        testSeminar.setId(1L);
        testSeminar.setName("Тестовый семинар");
        testSeminar.setSeminarCategoryEnum("MATH");
        testSeminar.setChatIdOwner(ownerUser.getChatId());
        testSeminar.setTimestamp(new Timestamp(System.currentTimeMillis()));
        Set<User> participants = new HashSet<>();
        participants.add(testUser);
        testSeminar.setParticipants(participants);
    }

    @Test
    @DisplayName("Должен находить семинары по категории")
    void shouldGetSeminarsByCategory() {
        // given
        String category = "MATH";
        List<SeminarDTO> expectedSeminars = Arrays.asList(testSeminar);

        when(seminarsCacheManager.getAllSeminars()).thenReturn(expectedSeminars);

        // when
        List<SeminarDTO> result = seminarsManager.getByCategory(category);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSeminarCategoryEnum()).isEqualTo(category);
        verify(seminarsCacheManager, times(1)).getAllSeminars();
    }

    @Test
    @DisplayName("Должен находить семинары по владельцу")
    void shouldGetSeminarsByOwner() {
        // given
        Long ownerId = 111111L;
        testSeminar.setChatIdOwner(ownerId);
        List<SeminarDTO> expectedSeminars = Arrays.asList(testSeminar);

        when(seminarsCacheManager.getAllSeminars()).thenReturn(expectedSeminars);

        // when
        List<SeminarDTO> result = seminarsManager.getSeminarsByChatIdOwner(ownerId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getChatIdOwner()).isEqualTo(ownerId);
    }

    @Test
    @DisplayName("Должен отправлять уведомления участникам семинара")
    void shouldNotifyParticipants() {
        // given
        String message = "Важное уведомление";
        testSeminar.getParticipants().add(testUser);

        // when
        seminarsManager.notify(telegramBot, testSeminar, message);

        // then
        verify(seminarsCacheManager, times(1)).updateSeminar(testSeminar);
        verify(telegramBot, times(1)).sendMessage(
                eq(testUser.getChatId()),
                contains(message),
                eq("Markdown")
        );
        assertThat(testSeminar.getMessages()).contains(message);
    }

    @Test
    @DisplayName("Не должен отправлять уведомление владельцу семинара")
    void shouldNotNotifyOwner() {
        // given
        String message = "Тестовое уведомление";
        testSeminar.setChatIdOwner(testUser.getChatId());
        testSeminar.setParticipants(new HashSet<>(Arrays.asList(testUser)));

        // when
        seminarsManager.notify(telegramBot, testSeminar, message);

        // then
        verify(telegramBot, never()).sendMessage(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("Должен возвращать пустой список если нет семинаров нужной категории")
    void shouldReturnEmptyListWhenNoCategoryMatch() {
        // given
        String category = "PHYSICS";
        List<SeminarDTO> allSeminars = Arrays.asList(testSeminar); // testSeminar - это MATH
        when(seminarsCacheManager.getAllSeminars()).thenReturn(allSeminars);

        // when
        List<SeminarDTO> result = seminarsManager.getByCategory(category);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Должен возвращать пустой список если нет семинаров у владельца")
    void shouldReturnEmptyListWhenNoOwnerMatch() {
        // given
        Long nonExistentOwnerId = 999999L;
        List<SeminarDTO> allSeminars = Arrays.asList(testSeminar);
        when(seminarsCacheManager.getAllSeminars()).thenReturn(allSeminars);

        // when
        List<SeminarDTO> result = seminarsManager.getSeminarsByChatIdOwner(nonExistentOwnerId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Должен находить несколько семинаров одной категории")
    void shouldFindMultipleSeminarsOfSameCategory() {
        // given
        String category = "MATH";
        SeminarDTO seminar2 = new SeminarDTO();
        seminar2.setId(2L);
        seminar2.setName("Второй семинар");
        seminar2.setSeminarCategoryEnum("MATH");

        List<SeminarDTO> allSeminars = Arrays.asList(testSeminar, seminar2);
        when(seminarsCacheManager.getAllSeminars()).thenReturn(allSeminars);

        // when
        List<SeminarDTO> result = seminarsManager.getByCategory(category);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(s -> s.getSeminarCategoryEnum().equals("MATH"));
    }
}

