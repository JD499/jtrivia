package com.jd.jtrivia.services

import com.jd.jtrivia.repositories.CategoryRepository
import com.jd.jtrivia.repositories.ClueRepository
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.web.reactive.function.client.WebClientResponseException

// @Service
class DatabasePopulator(
    private val jServiceFetcher: JServiceFetcher,
    private val categoryRepository: CategoryRepository,
    private val clueRepository: ClueRepository
) {

  @Retryable(
      value = [WebClientResponseException.TooManyRequests::class],
      maxAttempts = 5000,
      backoff = Backoff(delay = 5000, multiplier = 1.5))
  fun populateDatabase() {
    val categories = jServiceFetcher.fetchCategories(10)
    println("Fetched categories from jService: ${categories.size}")
    categories.forEach { println("Category ID: ${it.id}, Title: ${it.title}") }

    val savedCategories = categoryRepository.saveAll(categories)
    println("Saved categories to database: ${savedCategories.size}")
    savedCategories.forEach { println("Saved Category ID: ${it.id}, Title: ${it.title}") }

    savedCategories.forEach { category ->
      val clues = jServiceFetcher.fetchCluesByCategory(category.id.toInt())
      println("Fetched clues for category ID ${category.id}: ${clues.size}")
      clues.forEach { println("Clue ID: ${it.id}, Question: ${it.question}") }

      val savedClues = clueRepository.saveAll(clues)
      println("Saved clues for category ID ${category.id}: ${savedClues.size}")
      savedClues.forEach { println("Saved Clue ID: ${it.id}, Question: ${it.question}") }
    }
    println("Done populating database!")
  }
}
