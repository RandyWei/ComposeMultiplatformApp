package icu.bughub.app.cm

import MainView
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import icu.bughub.shared.cache.DatabaseSchema
import service.DatabaseDriverFactory
import service.databaseSchema

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseSchema = DatabaseSchema(
            DatabaseDriverFactory(this).createDriver()
        )

        //设置状态栏透明色
        window.statusBarColor = Color.Transparent.value.toInt()

        val windowInsetsControllerCompat =
            WindowCompat.getInsetsController(window, window.decorView)
        //设置状态栏模式，防止因背景是白色而看不见文字：
        // 设置状态栏是否Light模式，true时字体为黑色,false时字体为白色
        windowInsetsControllerCompat.isAppearanceLightStatusBars = true
        setContent {
            MainView()
        }
    }
}