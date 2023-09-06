package com.jd.jtrivia

import com.jd.jtrivia.services.JServiceDataImporter
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DatabaseSeeder(private val jServiceDataImporter: JServiceDataImporter) : CommandLineRunner {

  override fun run(vararg args: String?) {
    jServiceDataImporter.importAllData()
  }
}

/*
@Component
class DatabaseSeeder(private val databasePopulator: DatabasePopulator) : CommandLineRunner {

    override fun run(vararg args: String?) {
        databasePopulator.populateDatabase()
    }
}

 */
