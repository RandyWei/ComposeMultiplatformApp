package viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import icu.bughub.shared.cache.Word
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import network.JsonException
import service.ImageService
import service.SentenceService
import service.StoreService
import service.TimeService

class HomeViewModel : ScreenModel {


    private val timeService = TimeService.instance
    private val imageService = ImageService.instance
    private val sentenceService = SentenceService.instance
    private val storeService = StoreService()

    private val _uiState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val uiState = _uiState

    init {
        dispatch(HomeAction.FetchData)
    }

    fun dispatch(action: HomeAction) {
        when (action) {
            is HomeAction.FetchData -> {
                coroutineScope.launch {
                    kotlin.runCatching {
                        val timeEntityDeferred = async { timeService.getTime() }

                        //查询本地存储的所有数据
                        //这是一个无声的视频，
                        //为了能在这个项目里使用导航，我们增加一个查询7天的数据，7天之前的数据
                        //需要跳到更多页面

                        //SELECT * FROM word ORDER BY id DESC LIMIT 0,7
                        //这个语句就查询到最新的7条数据，因为id是自增的，所以按id倒序就可以取到的
                        //最新的，但我们希望取到的是正序的数据，所以再进行一次正序排序
                        //SELECT * FROM 上面查询到的数据 as Result ORDER BY Result.id
                        //SELECT * FROM (SELECT * FROM word ORDER BY id DESC LIMIT 0,7) AS Result ORDER BY Result.id
                        // 注意：在.sq文件中所有sql关键字都要是大写的
                        //然后make一下项目
                        //把view model里的selectAll替换一下，就是查询7天的数据了


                        var words = storeService.select7Words()
                        val timeEntity = timeEntityDeferred.await()
                        //如果本地数据为空，或者没有当天数据的时候，从网络获取
                        if (words.isEmpty() || words.last().date != timeEntity.date) {
                            val imageEntityDeferred =
                                async { imageService.getImage().list.firstOrNull() }
                            val sentenceDeferred = async { sentenceService.sentences() }
                            val imageEntity = imageEntityDeferred.await()
                            val sentence = sentenceDeferred.await()
                            //获取数据成功后插入数据库
                            storeService.insert(
                                sentence.name,
                                sentence.from,
                                timeEntity.date,
                                timeEntity.weekday,
                                imageEntity?.url ?: ""
                            )
                            //再次查询所有数据
                            words = storeService.select7Words()
                        }

                        _uiState.value = HomeUIState.Success(words)
                    }.onFailure {
                        if (it is JsonException) {
                            _uiState.value = HomeUIState.Error(it.message)
                        } else {
                            _uiState.value = HomeUIState.Error("数据加载出错，点击重试")
                        }

                    }
                }
            }
        }
    }

}

/**
 * 界面动作
 *
 */
sealed class HomeAction {
    object FetchData : HomeAction()
}

/**
 * UI状态
 *
 */
sealed class HomeUIState {
    object Loading : HomeUIState()

    data class Success(
        val words: List<Word>
    ) : HomeUIState()

    data class Error(val message: String) : HomeUIState()
}