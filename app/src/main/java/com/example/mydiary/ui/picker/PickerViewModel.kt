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
        Log.i("MYTAGgettinglist", "adding $selectedmedia"  )

        return selectedmedia}

    fun itemclicked(diaryMedia: DiaryMedia) {

        if (selectedmedia.contains(diaryMedia)) {
            selectedmedia.remove(diaryMedia)
            Log.i("MYTAGtype", "removing $diaryMedia"  )

        } else {
            selectedmedia.add(diaryMedia)
            Log.i("MYTAGtype", "adding $diaryMedia"  )
            Log.i("MYTAGtypelist", "adding $selectedmedia"  )
        }
        Log.i("MYTAGtypelist", "addinggg $selectedmedia"  )

        myselectedmedia.postValue(selectedmedia)

    }
}