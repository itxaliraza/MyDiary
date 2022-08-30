package com.example.mydiary.ui.homeFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydiary.Database.entity.Diary
import com.example.mydiary.Database.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(var repository: DiaryRepository) : ViewModel() {

    val _currentQuery = MutableStateFlow<String>("")



    fun setQuery(query: String) {
        _currentQuery.value=query
    }

    val results: Flow<List<Diary>> =
        _currentQuery.flatMapLatest { query ->
            repository.searchdiary(query).stateIn(viewModelScope)

        }




}