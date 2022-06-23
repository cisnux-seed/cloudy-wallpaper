package com.cisnux.wallpaper_app.data.datasources

import com.cisnux.wallpaper_app.data.model.Wallpaper
import com.cisnux.wallpaper_app.data.model.ApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val API_KEY = "563492ad6f91700001000001b4a006914d9640ea99e90f06a6a781a3"

object WallpaperRemoteDataSource {
    private const val BASE_URL = "https://api.pexels.com"
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    // delegate by lazy that means the value is computed only on first access.
    val retrofitService: WallpaperService by lazy {
        retrofit.create(WallpaperService::class.java)
    }
}

interface WallpaperService {
    @Headers("Authorization: $API_KEY")
    @GET("/v1/search")
    suspend fun getWallpapersByKeyword(
        @Query("query") query: String,
        @Query("per_page") perPage: Int
    ): ApiResponse<Wallpaper>
}