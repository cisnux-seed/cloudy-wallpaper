package com.cisnux.wallpaper_app.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.cisnux.wallpaper_app.data.model.Wallpaper
import com.cisnux.wallpaper_app.data.repository.WallpaperRepository
import com.cisnux.wallpaper_app.presentation.utils.WallpaperApiStatus
import com.cisnux.wallpaper_app.utils.TAG
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class WallpaperViewModel(private val wallpaperRepository: WallpaperRepository) : ViewModel() {
    // mutable backing properties
    private val _natureWallpapersStatus = MutableLiveData<WallpaperApiStatus>()
    private val _neonWallpapersStatus = MutableLiveData<WallpaperApiStatus>()
    private val _rainCityWallpapersStatus = MutableLiveData<WallpaperApiStatus>()
    private val _searchWallpapersStatus = MutableLiveData<WallpaperApiStatus>()
    private val _natureWallpapers = MutableLiveData<List<Wallpaper>>()
    private val _neonWallpapers = MutableLiveData<List<Wallpaper>>()
    private val _rainCityWallpapers = MutableLiveData<List<Wallpaper>>()
    private val _isFavorite = MutableLiveData(false)
    private val _wallpapersByKeyword = MutableLiveData<List<Wallpaper>>()

    // immutable public properties
    val natureWallpapersStatus: LiveData<WallpaperApiStatus> = _natureWallpapersStatus
    val searchWallpapersStatus: LiveData<WallpaperApiStatus> = _searchWallpapersStatus
    val neonWallpapersStatus: LiveData<WallpaperApiStatus> = _neonWallpapersStatus
    val rainCityWallpapersStatus: LiveData<WallpaperApiStatus> = _rainCityWallpapersStatus
    val isFavorite: LiveData<Boolean> = _isFavorite
    val natureWallpapers: LiveData<List<Wallpaper>> = _natureWallpapers
    val neonWallpapers: LiveData<List<Wallpaper>> = _neonWallpapers
    val rainCityWallpapers: LiveData<List<Wallpaper>> = _rainCityWallpapers
    val wallpapersByKeyword: LiveData<List<Wallpaper>> = _wallpapersByKeyword
    val user = wallpaperRepository.user

    companion object Category {
        const val NATURE_KEYWORD = "natures"
        const val NEON_KEYWORD = "neon"
        const val RAIN_CITY_KEYWORD = "rain city"
        const val HOME_PER_PAGE = 7
        const val SEARCH_PER_PAGE = 32
    }

    init {
        getCategoriesWallpaper()
    }

    @Suppress("DeferredResultUnused")
    private fun getCategoriesWallpaper() = viewModelScope.launch {
        _natureWallpapersStatus.value = WallpaperApiStatus.Loading
        _neonWallpapersStatus.value = WallpaperApiStatus.Loading
        _rainCityWallpapersStatus.value = WallpaperApiStatus.Loading

        with(wallpaperRepository) {
            async {
                getWallpapersByKeyword(NATURE_KEYWORD, HOME_PER_PAGE)
                    ?.fold({ natureFailure ->
                        _natureWallpapersStatus.value = WallpaperApiStatus.Failed(natureFailure)
                    }, { natureWallpapers ->
                        _natureWallpapers.value = natureWallpapers
                        _natureWallpapersStatus.value = WallpaperApiStatus.Done
                    })
            }
            async {
                getWallpapersByKeyword(NEON_KEYWORD, HOME_PER_PAGE)
                    ?.fold({ neonFailure ->
                        _neonWallpapersStatus.value =
                            WallpaperApiStatus.Failed(failure = neonFailure)
                    }, { neonWallpapers ->
                        _neonWallpapers.value = neonWallpapers
                        _neonWallpapersStatus.value = WallpaperApiStatus.Done
                    })
            }
            async {
                getWallpapersByKeyword(RAIN_CITY_KEYWORD, HOME_PER_PAGE)
                    ?.fold({ rainCityFailure ->
                        _rainCityWallpapersStatus.value =
                            WallpaperApiStatus.Failed(failure = rainCityFailure)
                    }, { rainCityWallpapers ->
                        _rainCityWallpapers.value = rainCityWallpapers
                        _rainCityWallpapersStatus.value = WallpaperApiStatus.Done
                    })
            }
        }
    }


    fun searchWallpapers(keyword: String) = viewModelScope.launch {
        _searchWallpapersStatus.value = WallpaperApiStatus.Loading
        wallpaperRepository.getWallpapersByKeyword(keyword, SEARCH_PER_PAGE)?.fold({
            _searchWallpapersStatus.value = WallpaperApiStatus.Failed(failure = it)
            Log.e(TAG, it.message)
        }, {
            _wallpapersByKeyword.value = it
            _searchWallpapersStatus.value = WallpaperApiStatus.Done
        })
    }

    fun downloadImage(wallpaper: Wallpaper) = viewModelScope.launch {
        wallpaperRepository.downloadImage(wallpaper)?.fold({
            Log.e(TAG, it.message)
        }, {})
    }

    fun favoriteToggle() = _isFavorite.value?.let {
        _isFavorite.value = !it
    }
}

/**
 * create factory class to instantiate custom [WallpaperViewModel]
 * */
class WallpaperViewModelFactory(private val wallpaperRepository: WallpaperRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WallpaperViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WallpaperViewModel(wallpaperRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
