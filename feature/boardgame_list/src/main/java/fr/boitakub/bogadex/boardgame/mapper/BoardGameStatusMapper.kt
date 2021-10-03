package fr.boitakub.bogadex.boardgame.mapper

import fr.boitakub.architecture.Mapper
import fr.boitakub.bgg.client.UserBoardGameStatus
import fr.boitakub.bogadex.boardgame.model.CollectionStatus
import fr.boitakub.bogadex.boardgame.model.Wished
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardGameStatusMapper @Inject constructor() : Mapper<CollectionStatus, UserBoardGameStatus> {

    override fun map(source: UserBoardGameStatus): CollectionStatus {
        return CollectionStatus(
            source.own.toBoolean(),
            source.preordered.toBoolean(),
            source.prevowned.toBoolean(),
            source.fortrade.toBoolean(),
            Wished.from(source.wishlistpriority) ?: Wished.isWished(source.wishlist)
        )
    }

    private fun Int.toBoolean() = this >= 1
}
