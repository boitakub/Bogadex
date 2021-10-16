package fr.boitakub.bogadex.boardgame.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.boitakub.architecture.Presenter
import fr.boitakub.bogadex.boardgame.BoardGameRepository
import fr.boitakub.bogadex.boardgame.model.BoardGame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardGameDetailViewModel @Inject constructor(
    private val repository: BoardGameRepository
) : ViewModel(), Presenter {

    private val _boardgame = MutableStateFlow(BoardGame())
    val boardgame: StateFlow<BoardGame> = _boardgame

    fun load(id: String) = effect {
        _boardgame.value = repository.loadBoardGameById(id)
    }

    private fun effect(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) { block() }
    }
}
