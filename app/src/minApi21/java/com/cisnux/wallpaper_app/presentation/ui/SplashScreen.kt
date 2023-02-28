package com.cisnux.wallpaper_app.presentation.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.cisnux.wallpaper_app.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

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
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            delay(1500L)
            Intent(this@SplashScreen, HomeActivity::class.java).let {
                this@SplashScreen.startActivity(it)
            }
        }
    }
}