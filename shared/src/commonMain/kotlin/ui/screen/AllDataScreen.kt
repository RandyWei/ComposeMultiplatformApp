package ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

//导航下的页面，都要继承Screen，不是继承，是实现
//注意这里要是object，当然如果是需要传参数的话，可以使用data class
object AllDataScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        Scaffold(topBar = {
            CenterAlignedTopAppBar(title = {
                Text("7天前数据")
            }, navigationIcon = {
                IconButton(onClick = {
                    //返回
                    navigator.pop()
                }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            })
        }, containerColor = Color.Green) {
            //这里我就不往下写了
            Text("这是更多数据页面", modifier = Modifier.padding(it))
        }
    }
}