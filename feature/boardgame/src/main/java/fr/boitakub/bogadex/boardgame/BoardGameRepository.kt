package fr.boitakub.bogadex.boardgame

import androidx.annotation.WorkerThread
import fr.boitakub.bgg_api_client.BggService
import fr.boitakub.clean_architecture.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BoardGameRepository @Inject constructor(
    override val local: BoardGameDao,
    override val remote: BggService
) : Repository {

    @WorkerThread
    fun loadBoardGameById(id: String) = flow {
        val movie = local.getBoardGameWithId(id)
        emit(movie)
    }.flowOn(Dispatchers.IO)
}
