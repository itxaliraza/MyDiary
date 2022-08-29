package com.example.mydiary.Database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mydiary.ui.picker.models.DiaryMedia
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Notes")
data class Diary(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String, var des: String,
    val timeStamp: String,
    val mood: Int? = null,
    val media: ArrayList<DiaryMedia>
) : Parcelable