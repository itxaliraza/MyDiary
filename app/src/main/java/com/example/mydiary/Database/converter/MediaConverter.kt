package com.example.mydiary.Database.converter

import androidx.room.TypeConverter
import com.example.mydiary.ui.picker.models.DiaryMedia
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MediaConverter {
        @TypeConverter
        fun fromList(value : ArrayList<DiaryMedia>) = Json.encodeToString(value)

        @TypeConverter
        fun toList(value: String) = Json.decodeFromString<ArrayList<DiaryMedia>>(value)
    }

