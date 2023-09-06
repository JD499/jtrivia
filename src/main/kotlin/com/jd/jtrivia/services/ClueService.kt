package com.jd.jtrivia.services

import com.jd.jtrivia.models.Clue
import com.jd.jtrivia.repositories.ClueRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.random.Random

@Service
class ClueService(private val clueRepository: ClueRepository) {

  fun getCluesByParameters(
      value: Int?,
      categoryId: Int?,
      minDate: LocalDate?,
      maxDate: LocalDate?,
      offset: Int
  ): List<Clue> {

    return clueRepository
        .findAll()
        .filter { clue ->
          (value == null || clue.value == value) &&
              (categoryId == null || clue.category.id == categoryId.toLong()) &&
              (minDate == null || !clue.airdate.isBefore(minDate)) &&
              (maxDate == null || !clue.airdate.isAfter(maxDate))
        }
        .drop(offset)
  }

  fun getRandomClues(count: Int): List<Clue> {
    val allClues = clueRepository.findAll()
    return List(count) { allClues[Random.nextInt(allClues.size)] }
  }

  fun getFinalJeopardy(count: Int): List<Clue> {

    val finalJeopardyClues = clueRepository.findAll().filter { it.value == null }
    return List(count) { finalJeopardyClues[Random.nextInt(finalJeopardyClues.size)] }
  }

  fun getCluesByCategoryId(categoryId: Int): List<Clue> {
    return clueRepository.findAll().filter { it.category.id == categoryId.toLong() }
  }
}
