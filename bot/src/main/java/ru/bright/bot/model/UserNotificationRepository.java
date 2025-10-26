package ru.bright.bot.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface UserNotificationRepository extends CrudRepository<UserNotification, Long> {


    @Query("SELECT un FROM UserNotification un " +
            "JOIN FETCH un.seminar s " +
            "JOIN FETCH un.user u " +
            "WHERE un.isNotified = false " +
            "AND un.isActive = true " +
            "AND s.timestamp IS NOT NULL " +
            "AND s.timestamp > :currentTime")
    List<UserNotification> findActiveNotifications(@Param("currentTime") Timestamp currentTime);

    @Query("SELECT un FROM UserNotification un " +
            "JOIN FETCH un.seminar " +
            "JOIN FETCH un.user " +
            "WHERE un.seminar.id = :seminarId " +
            "AND un.user.chatId = :userChatId")
    List<UserNotification> findBySeminarAndUser(@Param("seminarId") long seminarId,
                                                  @Param("userChatId") long chatId);

}
