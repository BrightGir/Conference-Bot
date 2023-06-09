package ru.bright.SpringConferenceBot.model;

import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Scope("singleton")
public interface SeminarRepository extends CrudRepository<ScienceSeminar, Long> {
    ArrayList<ScienceSeminar> findBySeminarCategoryEnum(String category);

    @Query(value = "SELECT MAX(t.id)+1 FROM science_seminars_table t WHERE NOT EXISTS (SELECT t.id FROM science_seminars_table WHERE id = t.id + 1)", nativeQuery = true)
    Long findFirstAvailableId();

    ArrayList<ScienceSeminar> findByChatIdOwner(Long chatId);
}
