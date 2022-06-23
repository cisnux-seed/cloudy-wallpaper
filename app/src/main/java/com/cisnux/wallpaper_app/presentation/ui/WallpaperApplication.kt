package com.cisnux.wallpaper_app.presentation.ui

import android.app.Application
import com.cisnux.wallpaper_app.data.repository.WallpaperRepository
import com.google.android.material.color.DynamicColors

// initiate repository and apply dynamic colors before any other class initiate
class WallpaperApplication : Application() {

    // only created when needed
    val repository: WallpaperRepository by lazy {
        WallpaperRepository(this)
    }

    override fun onCreate() {
        DynamicColors.applyToActivitiesIfAvailable(this)
        super.onCreate()
    }
}