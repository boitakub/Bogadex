package fr.boitakub.bogadex.boardgame.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.boitakub.architecture.Presenter
import fr.boitakub.bogadex.boardgame.BoardGameRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class BoardGameDetailViewModel @Inject constructor(
    private val repository: BoardGameRepository
) : ViewModel(), Presenter {

    private val boardGameIdSharedFlow: MutableSharedFlow<String> = MutableSharedFlow(replay = 1)

    val boardGameFlow = boardGameIdSharedFlow.flatMapLatest {
        repository.loadBoardGameById(it)
    }

    fun fetchBoardGameDetailsById(id: String) = boardGameIdSharedFlow.tryEmit(id)
}
