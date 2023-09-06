package com.jd.jtrivia.dto

import jakarta.persistence.Column
import java.time.LocalDate
import java.time.LocalDateTime

data class ClueResponse(
    val id: Long,
    val answer: String,
    @Column(length = 1000) val question: String,
    val airdate: LocalDate,
    val shown: Boolean,
    val categoryId: Long,
    val value: Int? = null,
    val game_id: Int? = null,
    val invalid_count: Int? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
