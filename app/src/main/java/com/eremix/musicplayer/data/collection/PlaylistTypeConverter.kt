package com.eremix.musicplayer.data.collection

import androidx.room.TypeConverter

class PlaylistTypeConverter {
    @TypeConverter
    fun fromList(list: List<Int>): String {
        return list.joinToString(",") // Преобразуем список в строку "1,2,3"
    }

    @TypeConverter
    fun toList(data: String): List<Int> {
        return if (data.isEmpty()) emptyList()
        else data.split(",").map { it.toInt() } // Преобразуем обратно в List<Int>
    }
}
