package service

import app.cash.sqldelight.db.SqlDriver
import icu.bughub.shared.cache.DatabaseSchema

expect class DatabaseDriverFactory {
    fun createDriver():SqlDriver
}

lateinit var databaseSchema: DatabaseSchema