package com.cisnux.wallpaper_app.data.model

import android.os.Parcelable
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