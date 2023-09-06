package com.jd.jtrivia.models

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class Category(
    @Id val id: Long,
    val title: String,
    @JsonProperty("clues_count") val cluesCount: Int,
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at") val updatedAt: LocalDateTime = LocalDateTime.now()
)
