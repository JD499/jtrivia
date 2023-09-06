package com.jd.jtrivia.extensions

import com.jd.jtrivia.dto.CategoryResponse
import com.jd.jtrivia.models.Category

fun Category.toResponse(): CategoryResponse {
  return CategoryResponse(id = this.id, title = this.title, clues_count = this.cluesCount)
}
