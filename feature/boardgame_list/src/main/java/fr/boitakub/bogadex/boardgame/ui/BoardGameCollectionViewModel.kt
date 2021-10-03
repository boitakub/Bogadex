package fr.boitakub.bogadex.boardgame.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import fr.boitakub.architecture.Presenter
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import fr.boitakub.bogadex.boardgame.usecase.ListCollection
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion

class BoardGameCollectionViewModel @AssistedInject constructor(
    @Assisted private val repository: ListCollection,
    provideExampleBggAccount: String,
) :
    ViewModel(), Presenter {

    @AssistedFactory
    interface BoardGameCollectionViewModelFactory {
        fun create(repository: ListCollection): BoardGameCollectionViewModel
    }

    val gameList: LiveData<List<CollectionItemWithDetails>> =
        repository.apply(provideExampleBggAccount)
            .catch { e ->
                e.message?.let { onError(it) }
            }
            .onCompletion {
                loading.value = false
            }
            .asLiveData(viewModelScope.coroutineContext)

    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    companion object {
        fun provideFactory(
            assistedFactory: BoardGameCollectionViewModelFactory,
            repository: ListCollection
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(repository) as T
                }
            }
    }
}
