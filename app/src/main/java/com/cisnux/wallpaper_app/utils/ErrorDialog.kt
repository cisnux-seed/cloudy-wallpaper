package com.cisnux.wallpaper_app.utils

import android.app.Activity
import com.cisnux.wallpaper_app.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun dialog(context: Activity, failure: Failure) = MaterialAlertDialogBuilder(context)
    .setTitle(failure.title)
    .setIcon(R.drawable.ic_no_internet)
    .setCancelable(false)
    .setMessage(failure.message)
    .setPositiveButton(context.resources.getString(R.string.retry_dialog)) { _, _ ->
        context.apply {
            com.cisnux.wallpaper_app.presentation.utils.errorDialog = null
            finish()
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }
    .create()