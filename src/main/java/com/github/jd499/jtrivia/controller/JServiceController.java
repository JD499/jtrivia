package com.github.jd499.jtrivia.controller;

import com.github.jd499.jtrivia.model.Category;
import com.github.jd499.jtrivia.model.Clue;
import com.github.jd499.jtrivia.repository.CategoryRepository;
import com.github.jd499.jtrivia.repository.ClueRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "JTrivia API", description = "Jeopardy! trivia game API endpoints")
public class JServiceController {
    private static final int MAX_COUNT = 100;
    private final ClueRepository clueRepository;
    private final CategoryRepository categoryRepository;

    @Operation(
            summary = "Get clues with filters",
            description = "Retrieve clues with optional filters for value, category, date range, and pagination"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved clues")
    @GetMapping("/clues")
    public ResponseEntity<List<Clue>> getClues(
            @Parameter(description = "Clue value in dollars")
            @RequestParam(required = false) Integer value,

            @Parameter(description = "Category ID")
            @RequestParam(required = false) Integer category,

            @Parameter(description = "Minimum air date (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) LocalDateTime min_date,

            @Parameter(description = "Maximum air date (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) LocalDateTime max_date,

            @Parameter(description = "Number of results to skip")
            @RequestParam(defaultValue = "0") int offset) {

        return ResponseEntity.ok(clueRepository.findCluesWithFilters(
                value, category, min_date, max_date, MAX_COUNT, offset));
    }

    @Operation(
            summary = "Get random clues",
            description = "Retrieve a specified number of random clues"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved random clues")
    @GetMapping("/random")
    public ResponseEntity<List<Clue>> getRandomClues(
            @Parameter(description = "Number of clues to return (max 100)")
            @RequestParam(defaultValue = "1") int count) {

        int limitedCount = Math.min(count, MAX_COUNT);
        return ResponseEntity.ok(clueRepository.findRandomClues(limitedCount));
    }

    @Operation(
            summary = "Get Final Jeopardy! clues",
            description = "Retrieve a specified number of random Final Jeopardy! questions"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved Final Jeopardy! clues")
    @GetMapping("/final")
    public ResponseEntity<List<Clue>> getFinalClues(
            @Parameter(description = "Number of Final Jeopardy! clues to return (max 100)")
            @RequestParam(defaultValue = "1") int count) {

        int limitedCount = Math.min(count, MAX_COUNT);
        return ResponseEntity.ok(clueRepository.findRandomFinalClues(limitedCount));
    }

    @Operation(
            summary = "Get categories",
            description = "Retrieve a list of categories with pagination support"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved categories")
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories(
            @Parameter(description = "Number of categories to return (max 100)")
            @RequestParam(defaultValue = "100") int count,

            @Parameter(description = "Number of categories to skip")
            @RequestParam(defaultValue = "0") int offset) {

        int limitedCount = Math.min(count, MAX_COUNT);
        return ResponseEntity.ok(categoryRepository.findCategoriesWithOffset(limitedCount, offset));
    }

    @Operation(
            summary = "Get clues by category",
            description = "Retrieve all clues for a specific category"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved category clues")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @GetMapping("/category")
    public ResponseEntity<List<Clue>> getCategory(
            @Parameter(description = "Category ID", required = true)
            @RequestParam int id) {

        return ResponseEntity.ok(clueRepository.findByCategoryId(id));
    }
}