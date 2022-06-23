package com.cisnux.wallpaper_app.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cisnux.wallpaper_app.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    companion object {
        const val USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@AboutActivity
            binding.aboutToolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            user = intent.getParcelableExtra(USER)
        }.also {
            setContentView(it.root)
        }
    }
}