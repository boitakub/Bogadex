package fr.boitakub.bogadex.boardgame.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.boitakub.architecture.BusinessModel
import java.util.Date

@Entity(tableName = "collection_item")
data class CollectionItem(
    @PrimaryKey @ColumnInfo(name = "bgg_id") var bggId: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "year_published") var yearPublished: Int = 0,
    @ColumnInfo(name = "cover_url") var coverUrl: String? = "",
    @ColumnInfo(name = "update_date") var updateDate: Date = Date(),
    @Embedded var status: CollectionStatus = CollectionStatus()
) : BusinessModel
