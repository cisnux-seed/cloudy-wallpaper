package com.cisnux.wallpaper_app.presentation.utils

import com.cisnux.wallpaper_app.utils.Failure

sealed class WallpaperApiStatus {
    class Failed(val failure: Failure) : WallpaperApiStatus()
object Loading : WallpaperApiStatus()
    object Done : WallpaperApiStatus()
}