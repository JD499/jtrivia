package com.jd.jtrivia.controllers

import com.jd.jtrivia.dto.CategoryResponse
import com.jd.jtrivia.extensions.toResponse
import com.jd.jtrivia.services.CategoryService
import com.jd.jtrivia.services.ClueService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ApiCategoryController(
    private val categoryService: CategoryService,
    private val clueService: ClueService
) {

  @GetMapping("/categories")
  fun getCategories(
      @RequestParam(value = "count", defaultValue = "1") count: Int,
      @RequestParam(value = "offset", defaultValue = "0") offset: Int
  ): List<CategoryResponse> {
    return categoryService.getCategories(count, offset).map { it.toResponse() }
  }

  @GetMapping("/category")
  fun getCategoryById(@RequestParam("id") id: Int): ResponseEntity<Map<String, Any>> {
    val category = categoryService.getCategoryById(id)
    val clues =
        clueService.getCluesByCategoryId(id).map { clue ->
          mapOf(
              "id" to clue.id,
              "answer" to clue.answer,
              "question" to clue.question,
              "airdate" to clue.airdate.toString(),
              "category_id" to clue.category.id,
              "value" to clue.value,
              "game_id" to clue.game_id,
              "invalid_count" to clue.invalid_count)
        }

    if (category != null) {
      val response =
          mapOf(
              "id" to category.id,
              "title" to category.title,
              "clues_count" to category.cluesCount,
              "clues" to clues)
      return ResponseEntity.ok(response)
    } else {
      return ResponseEntity.notFound().build()
    }
  }
}
