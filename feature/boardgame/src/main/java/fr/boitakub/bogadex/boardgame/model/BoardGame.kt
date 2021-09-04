package fr.boitakub.bogadex.boardgame.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.boitakub.clean_architecture.BusinessModel
import java.util.Date

@Entity(tableName = "boardgame")
data class BoardGame(
    @PrimaryKey @ColumnInfo(name = "bgg_id") var bggId: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "year_published") var yearPublished: Int = 0,
    @ColumnInfo(name = "min_player") var minPlayer: Int = 0,
    @ColumnInfo(name = "max_player") var maxPlayer: Int = 0,
    @ColumnInfo(name = "play_time") var playTime: Int = 0,
    @ColumnInfo(name = "min_play_time") var minPlayTime: Int = 0,
    @ColumnInfo(name = "max_play_time") var maxPlayTime: Int = 0,
    @ColumnInfo(name = "cover_url") var coverUrl: String? = "",
    @ColumnInfo(name = "update_date") var updateDate: Date = Date(),
    @Embedded var statistic: BoardGameBggStatistic = BoardGameBggStatistic()
) : BusinessModel
