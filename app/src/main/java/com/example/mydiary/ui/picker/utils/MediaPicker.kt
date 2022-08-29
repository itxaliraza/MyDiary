package com.example.mydiary.ui.picker.utils

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import com.example.mydiary.ui.picker.models.Cons
import com.example.mydiary.ui.picker.models.MediaFile
import com.example.mydiary.ui.picker.models.MediaFolder
import java.util.*


class MediaPicker(private val context: Context,var mediaType: String) {
    private fun getProjections(): Array<String> {
        return when (mediaType) {
            Cons.IMAGE -> arrayOf(

                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,//e.g. image file name

            )
            Cons.VIDEO -> arrayOf(
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,

                )
            Cons.AUDIO -> arrayOf(
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.AudioColumns.DURATION,


                )
            else -> arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.PARENT,
                MediaStore.Files.FileColumns.SIZE, MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Images.Media.BUCKET_ID,

                )
        }
    }

    private fun query(projection: Array<String>): Cursor? {
        return when (mediaType) {
            Cons.IMAGE -> {
                context.contentResolver.query(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                    } else {
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    },
                    projection,
                    null, null,
                    MediaStore.Images.Media.DATE_ADDED
                )
            }
            Cons.VIDEO -> context.contentResolver.query(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                } else {
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                },
                projection,
                null, null,

                MediaStore.Video.Media.DATE_ADDED
            )
            Cons.AUDIO -> context.contentResolver.query(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Audio.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL_PRIMARY
                    )
                } else {
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                },
                projection,
                null, null,
                MediaStore.Audio.Media.DATE_ADDED
            )
            else -> {
                null
            }
        }
    }

    fun queryMedia(): ArrayList<MediaFile> {
        val projection = getProjections()
        val cursor: Cursor? = query(projection)
        val list: ArrayList<MediaFile> = ArrayList<MediaFile>()

        cursor?.let {
            if (cursor.moveToLast()) {
                do {
                    val t: MediaFile = parse(cursor, projection)
                    list.add(t)
                } while (cursor.moveToPrevious())
            }

            cursor.close()
        }
        return list

    }

    private fun parse(cursor: Cursor, projection: Array<String>): MediaFile {

        val path = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]))
        val mime = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]))
        val folderId = cursor.getInt(cursor.getColumnIndexOrThrow(projection[2]))
        val foldername = cursor.getString(cursor.getColumnIndexOrThrow(projection[3]))

        val duration =
            if (mediaType == Cons.VIDEO || mediaType == Cons.AUDIO) {
                cursor.getLong(cursor.getColumnIndexOrThrow(projection[4]))
            } else {
                0
            }

        val mediaFile = MediaFile()
        mediaFile.path = path
        mediaFile.mimeType = mime
        mediaFile.folderId = folderId
        mediaFile.folderName = foldername
        mediaFile.duration = duration
        return mediaFile
    }

    fun getMediaFolder(
        imageFileList: ArrayList<MediaFile>? = null,
        videoFileList: ArrayList<MediaFile>? = null
    ): List<MediaFolder> {

        val mediaFolderMap: MutableMap<Int, MediaFolder> = HashMap()
        val mediaFiles = ArrayList<MediaFile>()
        if (imageFileList != null) {
            mediaFiles.addAll(imageFileList)
        }
        if (videoFileList != null) {
            mediaFiles.addAll(videoFileList)
        }



               if (!mediaFiles.isEmpty()) {
                   val mediaFolder = MediaFolder()
                   mediaFolder.folderId =
                       -1
                   mediaFolder.folderName = "Recent"
                   mediaFolder.folderCover = mediaFiles[0].path
                   mediaFolder.mediaFiles = mediaFiles
                   mediaFolderMap[-1] =
                       mediaFolder
               }

        if (imageFileList != null && !imageFileList.isEmpty()) {
            val size = imageFileList.size
            for (i in 0 until size) {
                val mediaFile = imageFileList[i]
                val folderId = mediaFile.folderId!!

                //先查看map 中是否存在当前的文件夹 ，不存在则创建
                var imageFolder = mediaFolderMap[folderId]
                if (imageFolder == null) {
                    imageFolder = MediaFolder()
                    imageFolder.folderId = folderId
                    imageFolder.folderName = mediaFile.folderName
                    imageFolder.folderCover = mediaFile.path
                    imageFolder.mediaFiles = ArrayList<MediaFile>()
                }
                val imageList = imageFolder.mediaFiles
                imageList.add(mediaFile)

                imageFolder.mediaFiles = imageList
                mediaFolderMap[folderId] = imageFolder
            }
        } else if (videoFileList != null && !videoFileList.isEmpty()) {
            val size = videoFileList.size
            for (i in 0 until size) {
                val mediaFile = videoFileList[i]
                val folderId = mediaFile.folderId!!

                //先查看map 中是否存在当前的文件夹 ，不存在则创建
                var imageFolder = mediaFolderMap[folderId]
                if (imageFolder == null) {
                    imageFolder = MediaFolder()
                    imageFolder.folderId = folderId
                    imageFolder.folderName = mediaFile.folderName
                    imageFolder.folderCover = mediaFile.path
                    imageFolder.mediaFiles = ArrayList<MediaFile>()
                }
                //取出MediaFolder的图片列表，把当前的图片资源添加进去
                val imageList = imageFolder.mediaFiles
                imageList.add(mediaFile)


                //重新再给图片资源列表复制
                imageFolder.mediaFiles = imageList
                mediaFolderMap[folderId] = imageFolder
            }
        }
        var mediaFolderList: List<MediaFolder> = mediaFolderMap.values.toList()

        Collections.sort(mediaFolderList, Comparator { o1: MediaFolder, o2: MediaFolder ->
            if (o1.mediaFiles.size > o2.mediaFiles.size) {
                -1
            } else if (o1.mediaFiles.size < o2.mediaFiles.size) {
                1
            } else {
                0
            }
        })
        return mediaFolderList


    }

}

