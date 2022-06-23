package com.cisnux.wallpaper_app.data.model

import android.content.Context
import android.graphics.Bitmap
import android.os.Parcelable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wallpaper(
    val id: String,
    val width: Int,
    val height: Int,
    val photographer: String,
    @Json(name = "alt")
    val desc: String,
    val src: ImageSize
) : Parcelable

// load image as bitmap from background thread
fun Wallpaper.getAsBitmap(context: Context): FutureTarget<Bitmap> =
    Glide.with(context).asBitmap().load(src.portrait).submit()