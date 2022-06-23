package com.cisnux.wallpaper_app.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val username: String,
    @DrawableRes val profilePict: Int,
    val email: String,
    val phoneNumber: String
) : Parcelable