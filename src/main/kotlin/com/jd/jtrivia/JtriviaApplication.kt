package com.jd.jtrivia

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EntityScan("com.jd.jtrivia.models")
@EnableJpaRepositories("com.jd.jtrivia.repositories")
@EnableRetry
class JtriviaApplication

fun main(args: Array<String>) {
  runApplication<JtriviaApplication>(*args)
}
