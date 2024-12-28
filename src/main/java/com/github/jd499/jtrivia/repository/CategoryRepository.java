package com.github.jd499.jtrivia.repository;

import com.github.jd499.jtrivia.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByTitle(String title);

    @Query(value = "SELECT * FROM categories LIMIT :count OFFSET :offset",
            nativeQuery = true)
    List<Category> findCategoriesWithOffset(
            @Param("count") int count,
            @Param("offset") int offset
    );
}