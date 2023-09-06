package com.jd.jtrivia.repositories

import com.jd.jtrivia.models.Clue
import org.springframework.data.jpa.repository.JpaRepository

interface ClueRepository : JpaRepository<Clue, Long>
