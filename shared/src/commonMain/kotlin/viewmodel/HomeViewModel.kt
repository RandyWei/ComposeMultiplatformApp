package viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import network.JsonException
import service.ImageService
import service.SentenceService
import service.TimeService

class HomeViewModel : ScreenModel {


    private val timeService = TimeService.instance
    private val imageService = ImageService.instance
    private val sentenceService = SentenceService.instance

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
                        val imageEntityDeferred =
                            async { imageService.getImage().list.firstOrNull() }
                        val sentenceDeferred = async { sentenceService.sentences() }

                        val timeEntity = timeEntityDeferred.await()
                        val imageEntity = imageEntityDeferred.await()
                        val sentence = sentenceDeferred.await()
                        println("timeEntity:$timeEntity")
                        println("imageEntity:$imageEntity")
                        println("sentence：$sentence")

                        _uiState.value = HomeUIState.Success(
                            sentence.name,
                            sentence.from,
                            timeEntity.date,
                            timeEntity.weekday,
                            imageEntity?.url ?: ""
                        )
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
        val content: String,
        val from: String,
        val date: String,
        val weekday: String,
        val imageUrl: String
    ) : HomeUIState()

    data class Error(val message: String) : HomeUIState()
}