package fr.boitakub.bogadex.boardgame.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.boitakub.bogadex.boardgame.model.CollectionItemWithDetails
import fr.boitakub.bogadex.boardgame.usecase.RetrieveBggUserCollection
import fr.boitakub.clean_architecture.Presenter
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

@HiltViewModel
class BoardGameCollectionViewModel @Inject constructor(
    repository: RetrieveBggUserCollection,
    provideExampleBggAccount: String,
) :
    ViewModel(), Presenter {

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
}
