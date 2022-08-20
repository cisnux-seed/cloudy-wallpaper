package com.cisnux.wallpaper_app.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.cisnux.wallpaper_app.data.model.User
import com.cisnux.wallpaper_app.databinding.ActivityHomeBinding
import com.cisnux.wallpaper_app.presentation.adapter.WallpapersAdapter
import com.cisnux.wallpaper_app.presentation.viewmodels.WallpaperViewModel
import com.cisnux.wallpaper_app.presentation.viewmodels.WallpaperViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val wallpaperViewModel: WallpaperViewModel by viewModels {
        WallpaperViewModelFactory((this.application as WallpaperApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@HomeActivity
            viewModel = wallpaperViewModel
            homeViewActivity = this@HomeActivity
            natureWallpaperList.apply {
                setHasFixedSize(true)
                adapter = WallpapersAdapter(this@HomeActivity)
            }
            neonWallpaperList.apply {
                setHasFixedSize(true)
                adapter = WallpapersAdapter(this@HomeActivity)
            }
            rainCityWallpaperList.apply {
                setHasFixedSize(true)
                adapter = WallpapersAdapter(this@HomeActivity)
            }
        }.also {
            setContentView(it.root)
        }
    }

    fun navigateToAbout(user: User) =
        Intent(this, AboutActivity::class.java)
            .apply {
                putExtra(AboutActivity.USER, user)
            }.also {
                startActivity(it)
            }

    fun navigateToSearch(isCategory: Boolean) =
        Intent(this, SearchActivity::class.java)
            .apply {
                putExtra(SearchActivity.IS_CATEGORY, isCategory)
            }.also {
                startActivity(it)
            }

    fun navigateToSearch(keyword: String?, isCategory: Boolean) =
        Intent(this, SearchActivity::class.java)
            .apply {
                putExtra(SearchActivity.IS_CATEGORY, isCategory)
                putExtra(SearchActivity.CATEGORY_TITLE, keyword)
            }.also {
                startActivity(it)
            }
}
