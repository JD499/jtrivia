package com.jd.jtrivia.controllers

import com.jd.jtrivia.services.ClueService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api")
class ApiClueController(private val clueService: ClueService) {

  @GetMapping("/clues")
  fun getClues(
      @RequestParam(value = "value", required = false) value: Int?,
      @RequestParam(value = "category", required = false) categoryId: Int?,
      @RequestParam(value = "min_date", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      minDate: LocalDate?,
      @RequestParam(value = "max_date", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      maxDate: LocalDate?,
      @RequestParam(value = "offset", defaultValue = "0") offset: Int
  ): ResponseEntity<List<Map<String, Any>>> {
    val clues =
        clueService.getCluesByParameters(value, categoryId, minDate, maxDate, offset).map { clue ->
          mutableMapOf(
                  "id" to clue.id,
                  "answer" to clue.answer,
                  "question" to clue.question,
                  "airdate" to clue.airdate.toString(),
                  "category_id" to clue.category.id,
                  "category" to
                      mapOf(
                          "id" to clue.category.id,
                          "title" to clue.category.title,
                          "created_at" to clue.category.createdAt.toString(),
                          "updated_at" to clue.category.updatedAt.toString(),
                          "clues_count" to clue.category.cluesCount))
              .apply {
                clue.value?.let { this["value"] = it }
                clue.game_id?.let { this["game_id"] = it }
                clue.invalid_count?.let { this["invalid_count"] = it }
              }
        }
    return ResponseEntity.ok(clues)
  }

  @GetMapping("/random")
  fun getRandomClues(
      @RequestParam(value = "count", defaultValue = "1") count: Int
  ): ResponseEntity<List<Map<String, Any>>> {
    val randomClues =
        clueService.getRandomClues(count).map { clue ->
          mutableMapOf(
                  "id" to clue.id,
                  "answer" to clue.answer,
                  "question" to clue.question,
                  "airdate" to clue.airdate.toString(),
                  "created_at" to clue.createdAt.toString(),
                  "updated_at" to clue.updatedAt.toString(),
                  "category_id" to clue.category.id,
                  "category" to
                      mapOf(
                          "id" to clue.category.id,
                          "title" to clue.category.title,
                          "created_at" to clue.category.createdAt.toString(),
                          "updated_at" to clue.category.updatedAt.toString(),
                          "clues_count" to clue.category.cluesCount))
              .apply {
                clue.value?.let { this["value"] = it }
                clue.game_id?.let { this["game_id"] = it }
                clue.invalid_count?.let { this["invalid_count"] = it }
              }
        }
    return ResponseEntity.ok(randomClues)
  }

  @GetMapping("/final")
  fun getFinalJeopardy(
      @RequestParam(value = "count", defaultValue = "1") count: Int
  ): ResponseEntity<List<Map<String, Any>>> {
    val finalJeopardyClues =
        clueService.getFinalJeopardy(count).map { clue ->
          mutableMapOf(
                  "id" to clue.id,
                  "answer" to clue.answer,
                  "question" to clue.question,
                  "airdate" to clue.airdate.toString(),
                  "created_at" to clue.createdAt.toString(),
                  "updated_at" to clue.updatedAt.toString(),
                  "category_id" to clue.category.id,
                  "category" to
                      mapOf(
                          "id" to clue.category.id,
                          "title" to clue.category.title,
                          "created_at" to clue.category.createdAt.toString(),
                          "updated_at" to clue.category.updatedAt.toString(),
                          "clues_count" to clue.category.cluesCount))
              .apply {
                clue.value?.let { this["value"] = it }
                clue.game_id?.let { this["game_id"] = it }
                clue.invalid_count?.let { this["invalid_count"] = it }
              }
        }
    return ResponseEntity.ok(finalJeopardyClues)
  }
}
