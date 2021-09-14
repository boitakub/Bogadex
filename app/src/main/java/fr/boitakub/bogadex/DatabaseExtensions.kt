package fr.boitakub.bogadex

import androidx.room.TypeConverter
import fr.boitakub.bogadex.boardgame.model.Wished
import fr.boitakub.bogadex.boardgame.model.Wished.Companion.toInt
import java.util.Date

class DatabaseExtensions {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun wishedToTnt(value: Wished) = value.toInt()
    @TypeConverter
    fun intTowished(value: Int) = Wished.from(value)
}
