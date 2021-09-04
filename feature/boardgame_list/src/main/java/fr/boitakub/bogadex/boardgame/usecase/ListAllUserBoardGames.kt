package fr.boitakub.bogadex.boardgame.usecase

import fr.boitakub.bogadex.boardgame.BoardGameDao
import fr.boitakub.bogadex.boardgame.model.BoardGame
import fr.boitakub.bogadex.boardgame.model.CollectionItem
import fr.boitakub.clean_architecture.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ListAllUserBoardGames @Inject constructor(
    private val retrieveBggUserCollection: RetrieveBggUserCollection,
    private val boardGamesData: BoardGameDao,
) : UseCase<Flow<List<BoardGame>>, String> {
    override fun apply(input: String): Flow<List<BoardGame>> {
        return flow {
            retrieveBggUserCollection.apply(input).collect {
                val result = it.map { collectionItem -> checkData(collectionItem) }
                emit(result)
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun checkData(collectionItem: CollectionItem): BoardGame {
        return if (boardGamesData.getBoardGameWithId(collectionItem.bggId) != null) {
            val boardgame = boardGamesData.getBoardGameWithId(collectionItem.bggId)
            boardgame.coverUrl = collectionItem.coverUrl
            boardgame.title = collectionItem.title
            boardgame
        } else {
            BoardGame(bggId = collectionItem.bggId, title = collectionItem.title, coverUrl = collectionItem.coverUrl)
        }
    }
}
