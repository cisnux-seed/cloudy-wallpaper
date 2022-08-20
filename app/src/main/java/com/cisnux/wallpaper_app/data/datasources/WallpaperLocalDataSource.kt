package com.cisnux.wallpaper_app.data.datasources

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.provider.MediaStore
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.cisnux.wallpaper_app.data.model.Wallpaper
import kotlinx.coroutines.Dispatchers

class WallpaperLocalDataSource(private val context: Context) {
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun saveWallpaperToGallery(wallpaper: Wallpaper) {
        val outputStream = context.contentResolver?.run {
            ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "${wallpaper.id}.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }.run {
                insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, this)?.let {
                    openOutputStream(it)
                }
            }
        }
        val result = context.imageLoader.execute(
            ImageRequest.Builder(context).data(wallpaper.src.original)
                .allowConversionToBitmap(true)
                .dispatcher(Dispatchers.IO)
                .build()
        )
        if (result is SuccessResult) {
            result.drawable.let {
                if (it is BitmapDrawable)
                    it.bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        outputStream
                    )
            }
        }
    }
}