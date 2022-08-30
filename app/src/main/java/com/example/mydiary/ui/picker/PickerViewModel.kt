package com.example.mydiary.ui.picker

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydiary.ui.picker.models.DiaryMedia

class PickerViewModel  : ViewModel() {
    private var selectedmedia: ArrayList<DiaryMedia> = arrayListOf()
      var myselectedmedia: MutableLiveData<ArrayList<DiaryMedia>> = MutableLiveData()
    init {
        myselectedmedia.postValue(arrayListOf())
    }

    fun getSelectedMedia(): ArrayList<DiaryMedia> {
        return selectedmedia}


}