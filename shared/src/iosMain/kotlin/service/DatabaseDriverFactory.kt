package service

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import icu.bughub.shared.cache.DatabaseSchema

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(DatabaseSchema.Schema, "data.db")
    }
}