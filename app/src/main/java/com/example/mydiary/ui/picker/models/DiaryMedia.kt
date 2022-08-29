package com.example.mydiary.ui.picker.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class DiaryMedia(var path: String, var type: String) : Parcelable

