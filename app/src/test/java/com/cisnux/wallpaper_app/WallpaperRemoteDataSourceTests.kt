package com.cisnux.wallpaper_app

import com.cisnux.wallpaper_app.data.datasources.BASE_URL
import com.cisnux.wallpaper_app.data.datasources.WallpaperApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class WallpaperApiServiceTests : BaseTest() {
    private lateinit var wallpaperApiService: WallpaperApiService
    private lateinit var query:String
    private var perPage:Int = 20

    @Before
    fun setup() {
        query = "nature"
        perPage = 2
        wallpaperApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("$BASE_URL?query=$query&per_page=$perPage"))
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .build()
            .create(WallpaperApiService::class.java)
    }

    @Test
    fun api_service() {
        // arrange
        mockResponse("wallpapers.json")
        runBlocking {
            // action
            val apiResponse = wallpaperApiService.getWallpapersByKeyword(query, perPage)

            // assert
            Assert.assertNotNull(apiResponse)
            Assert.assertTrue("API response wasn't empty", apiResponse.wallpapers.isNotEmpty())
            Assert.assertEquals(
                "photographer name didn't match",
                "Luis del Río",
                apiResponse.wallpapers.first().photographer
            )
        }
    }
}