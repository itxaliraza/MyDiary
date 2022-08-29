package com.example.mydiary.ui.picker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaFolder(
    var folderId: Int = 0,
    var folderName: String? = null,
    var folderCover: String? = null,
     var isCheck: Boolean = false,
    var mediaFiles: ArrayList<MediaFile> = arrayListOf()
) :Parcelable