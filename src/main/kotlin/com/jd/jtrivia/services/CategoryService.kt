package com.jd.jtrivia.services

import com.jd.jtrivia.models.Category
import com.jd.jtrivia.repositories.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

  fun getCategories(count: Int, offset: Int): List<Category> {
    return categoryRepository.findAll().drop(offset).take(count)
  }

  fun getCategoryById(id: Int): Category {
    return categoryRepository.findById(id.toLong()).orElseThrow {
      IllegalArgumentException("Category not found")
    }
  }
}
