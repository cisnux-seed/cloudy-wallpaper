package com.cisnux.wallpaper_app.presentation.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cisnux.wallpaper_app.R
import com.cisnux.wallpaper_app.data.model.Wallpaper
import com.cisnux.wallpaper_app.databinding.ActivityDetailBinding
import com.cisnux.wallpaper_app.presentation.viewmodels.WallpaperViewModel
import com.cisnux.wallpaper_app.presentation.viewmodels.WallpaperViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var wallpaper: Wallpaper? = null
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                wallpaper?.let { wallpaper ->
                    wallpaperViewModel.downloadImage(wallpaper)
                    showDownloadMessage()
                }
            }
        }
    private val wallpaperViewModel: WallpaperViewModel by viewModels {
        WallpaperViewModelFactory((this.application as WallpaperApplication).repository)
    }

    companion object {
        const val WALLPAPER = "wallpaper"
    }

    override fun onAttachedToWindow() {
        window.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                statusBarColor = Color.TRANSPARENT
                navigationBarColor = Color.TRANSPARENT
                setDecorFitsSystemWindows(false)
            } else {
                @Suppress("DEPRECATION")
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
            }
        }
        super.onAttachedToWindow()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wallpaper = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(WALLPAPER, Wallpaper::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(WALLPAPER)
        }
        binding = ActivityDetailBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@DetailActivity
            detailActivity = this@DetailActivity
            viewModel = wallpaperViewModel
            binding.detailToolbar.setNavigationOnClickListener {
                @Suppress("DEPRECATION")
                onBackPressed()
            }
            wallpaper = this@DetailActivity.wallpaper
        }.also {
            setContentView(it.root)
        }
    }

    fun getFavoriteIcon(isFavorite: Boolean): Int =
        if (!isFavorite) R.drawable.ic_favorite_border else R.drawable.ic_favorite

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED) -> {
                    wallpaper?.let { wallpaper ->
                        wallpaperViewModel.downloadImage(wallpaper)
                        showDownloadMessage()
                    }
                }
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(resources.getString(R.string.title_download_dialog))
                        .setMessage(resources.getString(R.string.message_download_dialog))
                        .setCancelable(true)
                        .setPositiveButton(resources.getString(R.string.accept_download_dialog)) { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }
                        .show()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        } else {
            wallpaper?.let { wallpaper ->
                wallpaperViewModel.downloadImage(wallpaper)
                showDownloadMessage()
            }
        }
    }

    private fun showDownloadMessage() =
        Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_SHORT)
            .show()

    fun shareWallpaper(wallpaper: Wallpaper) = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_wallpaper_subject))
        putExtra(Intent.EXTRA_TEXT, "${wallpaper.src.portrait} by ${wallpaper.photographer}")
    }
        .also { intent ->
            //  check to see if there's an app that could even handle it
            this.packageManager?.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)?.let {
                startActivity(intent)
            }
        }
}