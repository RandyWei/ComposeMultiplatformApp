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
import androidx.compose.material3.Button
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import service.DeviceService
import ui.components.PagerItem
import viewmodel.HomeAction
import viewmodel.HomeUIState
import viewmodel.HomeViewModel

const val MAX_LENGTH = 1 //为了能显示更多的页面，所以我们可以临时把这个值设置为1,因为我们当前数据库只有1条数据

object HomeScreen : Screen {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val homeViewModel = rememberScreenModel { HomeViewModel() }

        val uiState by homeViewModel.uiState.collectAsState()
        //再这里获取navigator对象
        val navigator = LocalNavigator.currentOrThrow

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
                //然后在页面渲染的时候，可以多给HorizontalPager的pageCount多一个size。这样就保证有8个页面，其中7个是正常的数据
                //还有一个是点击进入更多页面
                //
                //我们可以在这里就判断这个size应该是多少吧
                // 我们把这个 7 和 8 提成常量

                var size = words.size
                if (size == MAX_LENGTH) {
                    size += 1
                }

                val pagerState = rememberPagerState(size - 1) //因为上面已经加1了，所以这里还是减1
                HorizontalPager(
                    pageCount = size,
                    state = pagerState,
                    contentPadding = PaddingValues(
                        start = 32.dp,
                        end = 32.dp,
                        top = 32.dp,
                        bottom = DeviceService.getBottomSafeAreaHeight().dp + 32.dp
                    ),
                    pageSpacing = 16.dp
                ) { index ->
                    //判断当前是不是第0页，且当前总长度大于7或等于8的时候，才显示
                    // 数组越界了，是
                    if (index == 0 && size == MAX_LENGTH + 1) {
                        //这里我们可以放一个页面
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(onClick = {
                                //点击的是跳到新的页面去，我们先来加一个页面。
                                navigator.push(AllDataScreen) //直接写上object名字就可以了
                            }) {
                                Text("点击查看7天前的数据")
                            }
                        }
                    } else {
                        //这里的下标一样是index - 1，因为我们当前只有一条数据，所以index-1就不行了
                        //所以这里还得判断一下
                        var actualIndex = index
                        if (size == MAX_LENGTH + 1) {
                            actualIndex = index - 1
                        }
                        val word = words[actualIndex]
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

}

