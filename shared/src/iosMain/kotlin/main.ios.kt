import androidx.compose.ui.window.ComposeUIViewController
import icu.bughub.shared.cache.DatabaseSchema
import service.DatabaseDriverFactory
import service.databaseSchema

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController {
    databaseSchema = DatabaseSchema(DatabaseDriverFactory().createDriver())
    App()
}