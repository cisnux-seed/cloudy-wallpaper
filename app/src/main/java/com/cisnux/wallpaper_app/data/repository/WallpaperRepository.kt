package com.cisnux.wallpaper_app.data.repository

import android.content.ContentValues
import android.content.Context
import arrow.core.Either
import com.cisnux.wallpaper_app.R
import com.cisnux.wallpaper_app.data.datasources.WallpaperLocalDataSource
import com.cisnux.wallpaper_app.data.datasources.WallpaperRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.cisnux.wallpaper_app.data.model.User
import com.cisnux.wallpaper_app.data.model.Wallpaper
import com.cisnux.wallpaper_app.utils.Failure
import com.cisnux.wallpaper_app.utils.IOFailure
import com.cisnux.wallpaper_app.utils.RuntimeFailure
import java.io.IOException
import java.lang.RuntimeException

class WallpaperRepository(val context: Context) {
    val user: User
        get() = User(
            context.getString(R.string.full_name),
            context.getString(R.string.username),
            R.drawable.profile_pict,
            context.getString(R.string.email),
            context.getString(R.string.phone_number)
        )

    private val localDataSource: WallpaperLocalDataSource by lazy {
        WallpaperLocalDataSource(context)
    }

    suspend fun getWallpapersByKeyword(
        query: String,
        perPage: Int = 20
    ): Either<Failure, List<Wallpaper>>? =
        withContext(Dispatchers.IO) {
            try {
                Either.Right(
                    WallpaperRemoteDataSource.retrofitService.getWallpapersByKeyword(
                        query,
                        perPage
                    ).wallpapers
                )
            } catch (e: IOException) {
                Either.Left(
                    IOFailure(
                        context.getString(R.string.title_no_internet),
                        context.getString(R.string.message_no_internet)
                    )
                )
            } catch (e: RuntimeException) {
                e.message?.let { message ->
                    Either.Left(RuntimeFailure(null, message))
                }
            }
        }

    suspend fun downloadImage(wallpaper: Wallpaper): Either<Failure, ContentValues?>? =
        withContext(Dispatchers.IO) {
            try {
                Either.Right(localDataSource.saveWallpaperToGallery(wallpaper))
            } catch (e: IOException) {
                e.printStackTrace()
                e.message?.let { message ->
                    Either.Left(IOFailure(null, message))
                }
            } catch (e: RuntimeException) {
                e.printStackTrace()
                e.message?.let { message ->
                    Either.Left(RuntimeFailure(null, message))
                }
            }
        }

}