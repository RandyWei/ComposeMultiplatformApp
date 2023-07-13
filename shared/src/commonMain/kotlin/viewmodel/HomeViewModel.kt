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
                        var words = storeService.selectAll()
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
                            words = storeService.selectAll()
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