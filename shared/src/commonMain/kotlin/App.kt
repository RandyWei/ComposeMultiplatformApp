import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import ui.screen.HomeScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {
    MaterialTheme {
        Navigator(HomeScreen) {
            SlideTransition(it)
        }
    }
}

expect fun getPlatformName(): String