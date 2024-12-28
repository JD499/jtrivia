package com.github.jd499.jtrivia.repository;

import com.github.jd499.jtrivia.model.Clue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ClueRepository extends JpaRepository<Clue, Long> {

    @Query(value = """
           SELECT c.*
           FROM clues c
           WHERE (CAST(:value AS INTEGER) IS NULL OR c.value = CAST(:value AS INTEGER))
           AND (CAST(:categoryId AS INTEGER) IS NULL OR c.category_id = CAST(:categoryId AS INTEGER))
           AND (CAST(:minDate AS TIMESTAMP) IS NULL OR c.airdate >= CAST(:minDate AS TIMESTAMP))
           AND (CAST(:maxDate AS TIMESTAMP) IS NULL OR c.airdate <= CAST(:maxDate AS TIMESTAMP))
           ORDER BY c.id
           LIMIT CAST(:limit AS INTEGER)
           OFFSET CAST(:offset AS INTEGER)
           """, nativeQuery = true)
    List<Clue> findCluesWithFilters(
            @Param("value") Integer value,
            @Param("categoryId") Integer categoryId,
            @Param("minDate") LocalDateTime minDate,
            @Param("maxDate") LocalDateTime maxDate,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query(value = "SELECT * FROM clues WHERE round_type != 'FINAL_JEOPARDY' ORDER BY RANDOM() LIMIT :count",
            nativeQuery = true)
    List<Clue> findRandomClues(@Param("count") int count);

    @Query(value = "SELECT * FROM clues WHERE round_type = 'FINAL_JEOPARDY' ORDER BY RANDOM() LIMIT :count",
            nativeQuery = true)
    List<Clue> findRandomFinalClues(@Param("count") int count);

    List<Clue> findByCategoryId(Integer categoryId);
}