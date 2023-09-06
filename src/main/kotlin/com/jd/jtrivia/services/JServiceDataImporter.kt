package com.jd.jtrivia.services

import com.jd.jtrivia.repositories.CategoryRepository
import com.jd.jtrivia.repositories.ClueRepository
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException

@Service
class JServiceDataImporter(
    private val jServiceFetcher: JServiceFetcher,
    private val categoryRepository: CategoryRepository,
    private val clueRepository: ClueRepository
) {

  @Retryable(
      value = [WebClientResponseException.TooManyRequests::class],
      maxAttempts = 5000,
      backoff = Backoff(delay = 5000, multiplier = 1.5))
  fun importAllData() {
    var offset = 0
    val chunkSize = 100

    while (true) {
      val categories = jServiceFetcher.fetchCategoriesWithOffset(chunkSize, offset)
      if (categories.isEmpty()) {
        break
      }
      println("Fetched ${categories.size} categories with offset $offset")

      val savedCategories = categoryRepository.saveAll(categories)

      savedCategories.forEach { category ->
        val clues = jServiceFetcher.fetchCluesByCategory(category.id.toInt())
        clueRepository.saveAll(clues)
        println("Fetched and saved ${clues.size} clues for category ID ${category.id}")
      }

      offset += chunkSize
    }
    println("Done importing data!")
  }
}
