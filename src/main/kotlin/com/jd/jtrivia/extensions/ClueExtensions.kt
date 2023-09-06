package com.jd.jtrivia.extensions

import com.jd.jtrivia.dto.ClueResponse
import com.jd.jtrivia.models.Clue

fun Clue.toResponse() =
    ClueResponse(
        id = this.id,
        answer = this.answer,
        question = this.question,
        airdate = this.airdate,
        shown = this.shown,
        categoryId = this.category.id,
        value = this.value,
        game_id = this.game_id,
        invalid_count = this.invalid_count,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt)
