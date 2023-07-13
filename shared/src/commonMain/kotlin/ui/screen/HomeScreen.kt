package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import service.DeviceService
import ui.components.PagerItem
import viewmodel.HomeAction
import viewmodel.HomeUIState
import viewmodel.HomeViewModel


object HomeScreen : Screen {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val homeViewModel = rememberScreenModel { HomeViewModel() }

        val uiState by homeViewModel.uiState.collectAsState()



        when (uiState) {
            is HomeUIState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is HomeUIState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((uiState as HomeUIState.Error).message, modifier = Modifier.clickable {
                        homeViewModel.dispatch(HomeAction.FetchData)
                    })
                }
            }

            is HomeUIState.Success -> {
                val words = (uiState as HomeUIState.Success).words
                val pagerState = rememberPagerState(words.size - 1)
                HorizontalPager(
                    pageCount = words.size,
                    state = pagerState,
                    contentPadding = PaddingValues(
                        start = 32.dp,
                        end = 32.dp,
                        top = 32.dp,
                        bottom = DeviceService.getBottomSafeAreaHeight().dp + 32.dp
                    ),
                    pageSpacing = 16.dp
                ) { index ->
                    val word = words[index - 1]
                    PagerItem(
                        word.content,
                        word.from,
                        word.date,
                        word.weekday,
                        word.imageUrl
                    )

                }
            }
        }

    }

}

