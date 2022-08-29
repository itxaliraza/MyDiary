package com.example.mydiary.Database

import android.provider.ContactsContract
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mydiary.Database.converter.MediaConverter
import com.example.mydiary.Database.dao.DiaryDao
import com.example.mydiary.Database.entity.Diary

@TypeConverters(MediaConverter::class)
@Database(entities = [Diary::class], version = 1)
abstract class DiaryDB : RoomDatabase() {
    abstract fun getNotesDao(): DiaryDao

}