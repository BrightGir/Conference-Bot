package ru.bright.bot.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface UserNotificationRepository extends CrudRepository<UserNotification, Long> {


    @Query("SELECT un FROM userNotificationTable un " +
            "JOIN FETCH un.seminar s " +
            "JOIN FETCH un.user u " +
            "WHERE un.isNotified = false " +
            "AND un.isActive = true " +
            "AND s.timestamp IS NOT NULL " +
            "AND s.timestamp > :currentTime")
    List<UserNotification> findActiveNotifications(@Param("currentTime") Timestamp currentTime);

    @Query("SELECT un FROM userNotificationTable un " +
            "WHERE un.seminar = :seminar " +
            "AND un.user = :user")
    List<UserNotification> findBySeminarAndUser(@Param("seminar") ScienceSeminar seminar, 
                                                  @Param("user") User user);

    @Query("SELECT un FROM userNotificationTable un " +
            "WHERE un.seminar = :seminar")
    List<UserNotification> findBySeminar(@Param("seminar") ScienceSeminar seminar);

    void deleteBySeminar(ScienceSeminar seminar);

    @Query("SELECT un FROM userNotificationTable un " +
            "JOIN FETCH un.seminar s " +
            "WHERE un.user = :user")
    List<UserNotification> findByUser(@Param("user") User user);
}
