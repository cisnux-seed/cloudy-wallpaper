package com.cisnux.wallpaper_app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageSize(
    val original: String,
    val large: String,
    val small: String,
    val portrait: String,
    val landscape: String
):Parcelable
