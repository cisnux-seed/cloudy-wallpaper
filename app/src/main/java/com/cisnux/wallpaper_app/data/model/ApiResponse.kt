package com.cisnux.wallpaper_app.data.model

import com.squareup.moshi.Json

data class ApiResponse<out T>(
    @Json(name = "photos")
    val wallpapers: List<T>
)
