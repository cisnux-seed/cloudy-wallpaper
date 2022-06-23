package com.cisnux.wallpaper_app.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.cisnux.wallpaper_app.data.model.Wallpaper
import com.cisnux.wallpaper_app.data.repository.WallpaperRepository
import com.cisnux.wallpaper_app.presentation.utils.WallpaperApiStatus
import com.cisnux.wallpaper_app.utils.TAG
import kotlinx.coroutines.launch

class WallpaperViewModel(private val wallpaperRepository: WallpaperRepository) : ViewModel() {
    // mutable backing properties
    private val _status = MutableLiveData<WallpaperApiStatus>()
    private val _natureWallpapers = MutableLiveData<List<Wallpaper>>()
    private val _neonWallpapers = MutableLiveData<List<Wallpaper>>()
    private val _rainCityWallpapers = MutableLiveData<List<Wallpaper>>()
    private val _isFavorite = MutableLiveData(false)
    private val _wallpapersByKeyword = MutableLiveData<List<Wallpaper>>()

    // immutable public properties
    val status: LiveData<WallpaperApiStatus> = _status
    val isFavorite: LiveData<Boolean> = _isFavorite
    val natureWallpapers: LiveData<List<Wallpaper>> = _natureWallpapers
    val neonWallpapers: LiveData<List<Wallpaper>> = _neonWallpapers
    val rainCityWallpapers: LiveData<List<Wallpaper>> = _rainCityWallpapers
    val wallpapersByKeyword: LiveData<List<Wallpaper>> = _wallpapersByKeyword
    val user = wallpaperRepository.user

    init {
        getCategoriesWallpaper()
    }

    private fun getCategoriesWallpaper() = viewModelScope.launch {
        _status.value = WallpaperApiStatus.Loading
        wallpaperRepository.getWallpapersByKeyword("nature", 7)?.fold({ natureFailure ->
            _status.value = WallpaperApiStatus.Failed(natureFailure)
        }, { natureWallpapers ->
            _natureWallpapers.value = natureWallpapers
            wallpaperRepository.getWallpapersByKeyword("neon", 7)?.fold({ neonFailure ->
                _status.value = WallpaperApiStatus.Failed(failure = neonFailure)
            }, { neonWallpapers ->
                _neonWallpapers.value = neonWallpapers
                wallpaperRepository.getWallpapersByKeyword("rain city", 7)
                    ?.fold({ rainCityFailure ->
                        _status.value = WallpaperApiStatus.Failed(failure = rainCityFailure)
                    }, { rainCityWallpapers ->
                        _rainCityWallpapers.value = rainCityWallpapers
                        _status.value = WallpaperApiStatus.Done
                    })
            })
        })
    }

    fun searchWallpapers(keyword: String) = viewModelScope.launch {
        _status.value = WallpaperApiStatus.Loading
        wallpaperRepository.getWallpapersByKeyword(keyword)?.fold({
            _status.value = WallpaperApiStatus.Failed(failure = it)
            Log.e(TAG, it.message)
        }, {
            _wallpapersByKeyword.value = it
            _status.value = WallpaperApiStatus.Done
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
