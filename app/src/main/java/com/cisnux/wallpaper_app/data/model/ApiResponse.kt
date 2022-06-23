package com.cisnux.wallpaper_app.data.model

data class ApiResponse<out T>(
    val photos: List<T>
)
