package com.cisnux.wallpaper_app.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import com.cisnux.wallpaper_app.databinding.ActivitySearchBinding
import com.cisnux.wallpaper_app.presentation.adapter.WallpapersAdapter
import com.cisnux.wallpaper_app.presentation.viewmodels.WallpaperViewModel
import com.cisnux.wallpaper_app.presentation.viewmodels.WallpaperViewModelFactory
import com.cisnux.wallpaper_app.utils.hideKeyboard

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val wallpaperViewModel: WallpaperViewModel by viewModels {
        WallpaperViewModelFactory((this.application as WallpaperApplication).repository)
    }
    private var isCategory = false
    private var categoryTitle: String? = null

    companion object {
        const val IS_CATEGORY = "category"
        const val CATEGORY_TITLE = "category_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCategory = intent.getBooleanExtra(IS_CATEGORY, false)
        categoryTitle = intent.getStringExtra(CATEGORY_TITLE)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = this@SearchActivity
            searchActivity = this@SearchActivity
            viewModel = wallpaperViewModel
            searchWallpaperShimmer.viewModel = wallpaperViewModel
            searchWallpaperGrid.apply {
                setHasFixedSize(true)
                adapter = WallpapersAdapter(this@SearchActivity, true)
            }
            searchToolbar.setNavigationOnClickListener {
                this@SearchActivity.hideKeyboard()
                @Suppress("DEPRECATION")
                onBackPressed()
            }
            if (isCategory) {
                categoryTitle?.let {
                    wallpaperViewModel.searchWallpapers(it)
                    searchToolbar.title = it
                }
                searchBar.visibility = View.GONE
            } else {
                searchBar.apply {
                    /**
                     * call onOnViewExpanded to pop out search bar
                     * after user navigate from [HomeActivity] to [SearchActivity]
                     * */
                    onActionViewExpanded()
                    setOnQueryTextListener(object : OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            query?.let {
                                searchWallpaperGrid.visibility = View.GONE
                                this@SearchActivity.hideKeyboard()
                                wallpaperViewModel.searchWallpapers(query)
                            }
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean = false
                    })
                }
            }
        }.also {
            setContentView(it.root)
        }
    }
}