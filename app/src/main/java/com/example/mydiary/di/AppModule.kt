package com.example.mydiary.di

import android.content.Context
import androidx.room.Room
import com.example.mydiary.Database.DiaryDB
import com.example.mydiary.Database.dao.DiaryDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): DiaryDB {
        return Room.databaseBuilder(context, DiaryDB::class.java, "DiaryDB")
            .build()
    }


    @Provides
    @Singleton
    fun diarydao(diaryDB: DiaryDB): DiaryDao {
        return diaryDB.getNotesDao()
    }


}