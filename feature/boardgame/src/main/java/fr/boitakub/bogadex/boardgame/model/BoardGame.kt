package fr.boitakub.bogadex.boardgame.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.boitakub.clean_architecture.BusinessModel
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Entity(tableName = "boardgame")
data class BoardGame(
    @PrimaryKey @ColumnInfo(name = "bgg_id") var bggId: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "year_published") var yearPublished: Int = 0,
    @ColumnInfo(name = "min_player") var minPlayer: Int = 0,
    @ColumnInfo(name = "max_player") var maxPlayer: Int = 0,
    @ColumnInfo(name = "play_time") var playTime: Int = 0,
    @ColumnInfo(name = "min_play_time") var minPlayTime: Int = 0,
    @ColumnInfo(name = "max_play_time") var maxPlayTime: Int = 0,
    @ColumnInfo(name = "image") var image: String? = "",
    @ColumnInfo(name = "thumbnail") var thumbnail: String? = "",
    @ColumnInfo(name = "update_date") var updateDate: Date = Date(),
    @Embedded var statistic: BoardGameBggStatistic = BoardGameBggStatistic()
) : BusinessModel {

    fun isOutdated(): Boolean {
        val diffInMillies: Long = abs(Date().time - updateDate.time)
        val diff: Long = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)

        if (minPlayer == 0) {
            return true
        } else if (diff > 15) {
            return true
        }
        return false
    }
}
