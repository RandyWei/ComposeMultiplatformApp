package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import service.DeviceService
import ui.components.PagerItem
import viewmodel.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {

    val homeViewModel = HomeViewModel()

    HorizontalPager(
        pageCount = 10,
        contentPadding = PaddingValues(
            start = 32.dp,
            end = 32.dp,
            top = 32.dp,
            bottom = DeviceService.getBottomSafeAreaHeight().dp + 32.dp
        ),
        pageSpacing = 16.dp
    ) { index ->
        PagerItem(
            "今日的一句话",
            "韦爵爷",
            "2023-01-01",
            "星期二",
            "http://pic1.win4000.com/wallpaper/3/59acfc9214cca.jpg"
        )
    }
}