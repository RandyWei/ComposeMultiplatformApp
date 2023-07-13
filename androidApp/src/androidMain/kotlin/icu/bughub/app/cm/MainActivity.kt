package icu.bughub.app.cm

import MainView
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import icu.bughub.shared.cache.DatabaseSchema
import service.DatabaseDriverFactory
import service.databaseSchema

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseSchema = DatabaseSchema(
            DatabaseDriverFactory(this).createDriver()
        )

        setContent {
            MainView()
        }
    }
}