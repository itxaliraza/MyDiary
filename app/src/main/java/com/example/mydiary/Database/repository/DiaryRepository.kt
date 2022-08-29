package com.example.mydiary.Database.repository

import android.provider.ContactsContract
import com.example.mydiary.Database.dao.DiaryDao
import com.example.mydiary.Database.entity.Diary

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepository @Inject constructor (var diaryDao: DiaryDao) {
    suspend fun insertentry(diary: Diary) = diaryDao.insertentry(diary)
    suspend fun updateentry(diary: Diary) = diaryDao.updateentry(diary)
     fun getallentries() = diaryDao.getallentries()
     fun searchdiary(query:String) = diaryDao.searchdiary(query)
    suspend fun deleteentry(id: Int) = diaryDao.deleteentry(id)
}