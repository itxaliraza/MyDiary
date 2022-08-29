package com.example.mydiary.utils

import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.view.View
import android.widget.Toast
import androidx.annotation.StyleRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}




lateinit var dialog: androidx.appcompat.app.AlertDialog


fun Context.alert(
    @StyleRes style: Int = 0,
    dialogBuilder: MaterialAlertDialogBuilder.() -> Unit
) {


    dialog = MaterialAlertDialogBuilder(
        this, style
    )
        .apply {
            setCancelable(true)
            dialogBuilder()
        }.create()
    dialog.show()
}


fun MaterialAlertDialogBuilder.positiveButton(
    text: String = "Yes",
    handleClick: (dialogInterface: DialogInterface) -> Unit = { it.dismiss() }
) {
    this.setPositiveButton(text) { dialogInterface, _ -> handleClick(dialogInterface) }
}

fun MaterialAlertDialogBuilder.negativeButton(
    text: String = "No",
    handleClick: (dialogInterface: DialogInterface) -> Unit = { it.dismiss() }
) {
    this.setNegativeButton(text) { dialogInterface, _ -> handleClick(dialogInterface) }
}

fun dismissme() {
    dialog.dismiss()
}



val Int.px get() = (this * Resources.getSystem().displayMetrics.density).toInt()

