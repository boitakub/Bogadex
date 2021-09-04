package fr.boitakub.bogadex.boardgame.mapper

import fr.boitakub.bgg_api_client.UserBoardGame
import fr.boitakub.bgg_api_client.UserCollection
import fr.boitakub.bogadex.boardgame.model.CollectionItem
import fr.boitakub.clean_architecture.Mapper
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionMapper @Inject constructor(private val statusMapper: BoardGameStatusMapper) :
    Mapper<fr.boitakub.bogadex.boardgame.model.Collection, UserCollection> {

    override fun map(source: UserCollection): fr.boitakub.bogadex.boardgame.model.Collection {
        return fr.boitakub.bogadex.boardgame.model.Collection(map(source.games))
    }

    private fun map(entity: UserBoardGame): CollectionItem =
        CollectionItem(
            entity.objectid,
            entity.name,
            entity.yearpublished,
            entity.thumbnail,
            Date(),
            statusMapper.map(entity.status)
        )

    private fun map(list: List<UserBoardGame>): List<CollectionItem> = list.map { map(it) }
}
