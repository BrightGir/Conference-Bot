package ru.bright.bot.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public interface SeminarRepository extends JpaRepository<ScienceSeminar, Long> {

    @Query(value = "SELECT MAX(t.id)+1 FROM science_seminars_table t WHERE NOT EXISTS (SELECT t.id FROM science_seminars_table WHERE id = t.id + 1)", nativeQuery = true)
    Long findFirstAvailableId();

    @Query("SELECT s FROM ScienceSeminar s " +
           "LEFT JOIN FETCH s.participants p " +
           "LEFT JOIN FETCH s.messages " +
           "WHERE p.chatId = :userId")
    List<ScienceSeminar> findByUserIdWithParticipantsAndMessages(@Param("userId") Long userChatId);

    @Query("SELECT ss FROM ScienceSeminar ss " +
            "JOIN FETCH ss.participants " +
            "WHERE ss.timestamp < :now")
    List<ScienceSeminar> findExpiredSeminarsWithParticipants(@Param("now") Timestamp timestamp);

    @Query("SELECT s FROM ScienceSeminar s " +
           "LEFT JOIN FETCH s.participants " +
           "LEFT JOIN FETCH s.messages")
    List<ScienceSeminar> findAllWithParticipantsAndMessages();

    @Query("SELECT p FROM ScienceSeminar p " +
            "LEFT JOIN FETCH p.participants " +
            "LEFT JOIN FETCH p.messages " +
            "WHERE p.id = :id")
    Optional<ScienceSeminar> findByIdWithParticipantsAndMessages(@Param("id") Long id);

}
