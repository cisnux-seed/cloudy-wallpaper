package com.cisnux.wallpaper_app.utils

const val TAG = "wallpaper"

abstract class Failure(val title: String? = "Failure", val message: String)
class IOFailure(title: String?, message: String) : Failure(title, message)
class RuntimeFailure(title: String?, message: String) : Failure(title, message)
