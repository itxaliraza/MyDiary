package com.example.mydiary.ui.DetailsFragment

import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.Database.entity.Diary
import com.example.mydiary.Database.repository.DiaryRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(var repository: DiaryRepository) : ViewModel() {


    fun updatenote(diary:Diary) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateentry(diary)
        }
    }
    fun insertnote(diary:Diary) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertentry(diary)
        }
    }

    fun deletenote(id:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteentry(id)
        }
    }

}