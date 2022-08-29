package com.example.mydiary.Database.dao

import android.provider.ContactsContract
import androidx.room.*
import com.example.mydiary.Database.entity.Diary
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("Select * from notes")
    fun getallentries(): Flow<List<Diary>>

   @Query("Select * from notes where title like  '%' || :query || '%' or des like  '%' || :query || '%'")
    fun searchdiary(query:String): Flow<List<Diary>>

    @Query("delete from notes where id=:id")
    suspend fun deleteentry(id: Int)


    @Update
    suspend fun updateentry(diary: Diary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertentry(diary: Diary)


}