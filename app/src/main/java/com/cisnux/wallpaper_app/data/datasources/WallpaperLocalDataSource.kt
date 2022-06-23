package com.cisnux.wallpaper_app.data.datasources

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.cisnux.wallpaper_app.data.model.Wallpaper
import com.cisnux.wallpaper_app.data.model.getAsBitmap

class WallpaperLocalDataSource(private val context: Context) {
    fun saveWallpaperToGallery(wallpaper: Wallpaper) =
        context.contentResolver?.let { resolver ->
            ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "${wallpaper.id}.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }.also { contentValues ->
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)?.let {
                    resolver.openOutputStream(it)
                }?.use { outputStream ->
                    wallpaper.getAsBitmap(context).let { futureTarget ->
                        futureTarget.get()
                            ?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        // clear resource futureTarget
                        Glide.with(context).clear(futureTarget)
                    }
                }
            }
        }
}