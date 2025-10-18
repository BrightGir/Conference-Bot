package ru.bright.bot.model;

import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public interface SeminarRepository extends CrudRepository<ScienceSeminar, Long> {

    ArrayList<ScienceSeminar> findBySeminarCategoryEnum(String category);

    @Query(value = "SELECT MAX(t.id)+1 FROM science_seminars_table t WHERE NOT EXISTS (SELECT t.id FROM science_seminars_table WHERE id = t.id + 1)", nativeQuery = true)
    Long findFirstAvailableId();

    @Query("SELECT s FROM scienceSeminarsTable s JOIN s.participants p WHERE p.chatId = :userId")
    List<ScienceSeminar> findByUserId(@Param("userId") Long userChatId);

    @Query("SELECT ss FROM scienceSeminarsTable ss " +
            "WHERE ss.timestamp < :now")
    List<ScienceSeminar> findExpiredSeminars(@Param("now") Timestamp timestamp);

    @Query("SELECT s FROM scienceSeminarsTable s " +
           "LEFT JOIN FETCH s.participants")
    List<ScienceSeminar> findAllWithParticipants();

    @Query("SELECT p FROM scienceSeminarsTable p LEFT JOIN FETCH p.participants WHERE p.id = :id")
    Optional<ScienceSeminar> findByIdWithParticipants(@Param("id") Long id);

    ArrayList<ScienceSeminar> findByChatIdOwner(Long chatId);

}
