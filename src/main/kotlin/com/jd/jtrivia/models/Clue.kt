package com.jd.jtrivia.models

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class Clue(
    @Id val id: Long,
    val answer: String,
    @Column(length = 1000) val question: String,
    val airdate: LocalDate,
    val shown: Boolean = false,
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "category_id") val category: Category,
    @Column(name = "`VALUE`") val value: Int? = null,
    val game_id: Int? = null,
    val invalid_count: Int? = null,
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at") val updatedAt: LocalDateTime = LocalDateTime.now()
)
