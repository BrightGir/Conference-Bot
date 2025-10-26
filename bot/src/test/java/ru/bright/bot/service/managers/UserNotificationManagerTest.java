package ru.bright.bot.service.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bright.bot.model.*;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.utils.Role;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserNotificationManagerTest {

    @Mock
    private UserNotificationRepository userNotificationRepository;

    @Mock
    private SeminarRepository seminarRepository;

    @InjectMocks
    private UserNotificationManager userNotificationManager;

    private User testUser;
    private SeminarDTO testSeminar;
    private UserNotification testNotification;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setChatId(123456L);
        testUser.setFio("Иван Иванов");
        testUser.setRole(Role.USER);

        testSeminar = new SeminarDTO();
        testSeminar.setId(1L);
        testSeminar.setName("Тестовый семинар");
        testSeminar.setChatIdOwner(111111L);

        testNotification = new UserNotification();
        testNotification.setId(1L);
        testNotification.setUser(testUser);
        testNotification.setHour(24);
        testNotification.setActive(true);
        testNotification.setNotified(false);
    }

    @Test
    @DisplayName("Должен создавать новое уведомление если его нет")
    void shouldCreateNewNotificationWhenNotExists() {
        // given
        int hour = 3;
        ScienceSeminar scienceSeminar = new ScienceSeminar();
        scienceSeminar.setId(testSeminar.getId());

        when(userNotificationRepository.findBySeminarAndUser(testSeminar.getId(), testUser.getChatId()))
                .thenReturn(Collections.emptyList());
        when(seminarRepository.getReferenceById(testSeminar.getId()))
                .thenReturn(scienceSeminar);

        // when
        userNotificationManager.changeTimeNotify(testUser, testSeminar, hour);

        // then
        verify(userNotificationRepository, times(1)).save(any(UserNotification.class));
    }

    @Test
    @DisplayName("Должен переключать активность существующего уведомления")
    void shouldToggleExistingNotification() {
        // given
        int hour = 24;
        testNotification.setHour(hour);
        testNotification.setActive(true);
        testNotification.setNotified(false);

        when(userNotificationRepository.findBySeminarAndUser(testSeminar.getId(), testUser.getChatId()))
                .thenReturn(Arrays.asList(testNotification));

        // when
        userNotificationManager.changeTimeNotify(testUser, testSeminar, hour);

        // then
        assertThat(testNotification.isActive()).isFalse(); // должен переключиться
        verify(userNotificationRepository, times(1)).save(testNotification);
    }

    @Test
    @DisplayName("Не должен ничего делать если уведомление уже отправлено")
    void shouldDoNothingWhenNotificationAlreadySent() {
        // given
        int hour = 24;
        testNotification.setHour(hour);
        testNotification.setNotified(true); // уже отправлено

        when(userNotificationRepository.findBySeminarAndUser(testSeminar.getId(), testUser.getChatId()))
                .thenReturn(Arrays.asList(testNotification));

        // when
        userNotificationManager.changeTimeNotify(testUser, testSeminar, hour);

        // then
        verify(userNotificationRepository, never()).save(any(UserNotification.class));
    }

    @Test
    @DisplayName("Должен проверять наличие активного уведомления")
    void shouldCheckIfUserHasActiveNotification() {
        // given
        int hour = 24;
        testNotification.setHour(hour);
        testNotification.setActive(true);
        testNotification.setNotified(false);

        when(userNotificationRepository.findBySeminarAndUser(testSeminar.getId(), testUser.getChatId()))
                .thenReturn(Arrays.asList(testNotification));

        // when
        boolean result = userNotificationManager.hasNotify(testUser, testSeminar, hour);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Должен возвращать false если уведомление уже отправлено")
    void shouldReturnFalseWhenNotificationAlreadySent() {
        // given
        int hour = 24;
        testNotification.setHour(hour);
        testNotification.setNotified(true);

        when(userNotificationRepository.findBySeminarAndUser(testSeminar.getId(), testUser.getChatId()))
                .thenReturn(Arrays.asList(testNotification));

        // when
        boolean result = userNotificationManager.hasNotify(testUser, testSeminar, hour);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Должен возвращать false если уведомление неактивно")
    void shouldReturnFalseWhenNotificationIsInactive() {
        // given
        int hour = 24;
        testNotification.setHour(hour);
        testNotification.setActive(false);
        testNotification.setNotified(false);

        when(userNotificationRepository.findBySeminarAndUser(testSeminar.getId(), testUser.getChatId()))
                .thenReturn(Arrays.asList(testNotification));

        // when
        boolean result = userNotificationManager.hasNotify(testUser, testSeminar, hour);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Должен возвращать false если уведомление не найдено")
    void shouldReturnFalseWhenNotificationNotFound() {
        // given
        int hour = 24;

        when(userNotificationRepository.findBySeminarAndUser(testSeminar.getId(), testUser.getChatId()))
                .thenReturn(Collections.emptyList());

        // when
        boolean result = userNotificationManager.hasNotify(testUser, testSeminar, hour);

        // then
        assertThat(result).isFalse();
    }
}

