package com.example.mydiary.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.io.File


object StorageUtils {

    fun savefile(c: Context, path: String): String {

        var i = 1
        try {
            var p = c.filesDir.absoluteFile.toString() + "/" + path.split("/").last()

            val name = p.split("/").last().split(".")


            while (File(p).exists()) {

                    p =
                        c.filesDir.absoluteFile.toString() + "/" + name[0] + i.toString() + "." + name[1]
                    i++


            }
            File(path).inputStream().use { input ->
                c.openFileOutput(p.split("/").last(), MODE_PRIVATE).use {
                    input.copyTo(it)
                }
                return p

            }
        } catch (e: Exception) {

            return e.message.toString()
        }
    }

    fun deletefile(c: Context, path: String): String {

        try {
            var p = c.filesDir.absoluteFile.toString() + "/" + path.split("/").last()


            if (File(p).exists())
                File(p).delete()
            return "SUCCESS"


    } catch (e: Exception)
    {

        return e.message.toString()
    }
}


}

