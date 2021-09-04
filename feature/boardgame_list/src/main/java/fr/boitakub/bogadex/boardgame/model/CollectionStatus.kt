package fr.boitakub.bogadex.boardgame.model

import androidx.room.ColumnInfo
import fr.boitakub.clean_architecture.BusinessModel

data class CollectionStatus(
    @ColumnInfo(name = "is_own") val own: Boolean = false,
    @ColumnInfo(name = "is_pre_ordered") val preOrdered: Boolean = false,
    @ColumnInfo(name = "is_previously_owned") val previouslyOwned: Boolean = false,
    @ColumnInfo(name = "is_for_trade") val forTrade: Boolean = false,
    @ColumnInfo(name = "is_wished") val wished: Wished = Wished.NOT_WISHED
) : BusinessModel
