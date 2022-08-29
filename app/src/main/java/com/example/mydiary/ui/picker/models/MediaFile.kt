package com.example.mydiary.ui.picker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaFile(
    var path: String="",

    var mimeType: String? = null,
    var folderId: Int? = null,
    var folderName: String? = null,

    var duration: Long = 0,

    var updateTime: Long = 0,
    var checked: Boolean = false
) :Parcelable