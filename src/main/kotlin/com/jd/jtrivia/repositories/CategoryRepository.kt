package com.jd.jtrivia.repositories

import com.jd.jtrivia.models.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long>
