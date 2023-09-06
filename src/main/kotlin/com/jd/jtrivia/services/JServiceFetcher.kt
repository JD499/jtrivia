package com.jd.jtrivia.services

import com.jd.jtrivia.models.Category
import com.jd.jtrivia.models.Clue
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class JServiceFetcher(webClientBuilder: WebClient.Builder) {

  private val baseUrl = "http://jservice.io/api"
  private val webClient = webClientBuilder.baseUrl(baseUrl).build()

  fun fetchCategories(count: Int): List<Category> {
    val categories =
        webClient
            .get()
            .uri("/categories?count=$count")
            .retrieve()
            .bodyToFlux(Category::class.java)
            .collectList()
            .block()
            ?: emptyList()

    println("Fetched ${categories.size} categories:")

    return categories
  }

  fun fetchCluesByCategory(categoryId: Int): List<Clue> {
    val clues =
        webClient
            .get()
            .uri("/clues?category=$categoryId")
            .retrieve()
            .bodyToFlux(Clue::class.java)
            .collectList()
            .block()
            ?: emptyList()

    println("Fetched ${clues.size} clues for category ID $categoryId:")

    return clues
  }

  fun fetchCategoriesWithOffset(count: Int, offset: Int): List<Category> {
    return webClient
        .get()
        .uri("/categories?count=$count&offset=$offset")
        .retrieve()
        .bodyToFlux(Category::class.java)
        .collectList()
        .block()
        ?: emptyList()
  }
}
