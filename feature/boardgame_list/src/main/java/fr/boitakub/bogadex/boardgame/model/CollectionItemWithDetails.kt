package fr.boitakub.bogadex.boardgame.model

import androidx.room.Embedded
import androidx.room.Relation

data class CollectionItemWithDetails(
    @Embedded val item: CollectionItem,
    @Relation(
        entity = BoardGame::class,
        parentColumn = "bgg_id",
        entityColumn = "bgg_id"
    ) var details: BoardGame?
)
