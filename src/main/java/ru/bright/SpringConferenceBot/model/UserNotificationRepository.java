package ru.bright.SpringConferenceBot.model;

import org.springframework.data.repository.CrudRepository;

public interface UserNotificationRepository extends CrudRepository<UserNotification, Long> {
}
