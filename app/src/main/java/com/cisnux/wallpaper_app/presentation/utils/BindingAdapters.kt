package com.cisnux.wallpaper_app.presentation.utils


import android.app.Activity
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cisnux.wallpaper_app.R
import com.cisnux.wallpaper_app.data.model.Wallpaper
import com.cisnux.wallpaper_app.presentation.adapter.WallpapersAdapter
import com.cisnux.wallpaper_app.utils.IOFailure
import com.cisnux.wallpaper_app.utils.TAG
import com.cisnux.wallpaper_app.utils.dialog
import com.facebook.shimmer.ShimmerFrameLayout

var errorDialog: AlertDialog? = null

/**
 * submit listData to [WallpapersAdapter]
 * */
@BindingAdapter("listData", "status")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<Wallpaper>?,
    status: WallpaperApiStatus?
) {
    if (data != null && status is WallpaperApiStatus.Done) {
        recyclerView.visibility = View.VISIBLE
        val adapter = recyclerView.adapter as WallpapersAdapter
        adapter.submitList(data)
    } else if (status is WallpaperApiStatus.Failed) {
        if (status.failure is IOFailure) {
            if (errorDialog == null) {
                errorDialog = dialog(recyclerView.context as Activity, status.failure)
                errorDialog
                    ?.show()
            }
        }
        Log.e(TAG, status.failure.message)
    }
}

@BindingAdapter("wallpaperItemSrc")
fun setWallpaperItemSrc(imageView: ImageView, url: String?) =
    imageView.load(url) {
        crossfade(true)
    }

@BindingAdapter("wallpaperDetailSrc")
fun setWallpaperDetailSrc(imageView: ImageView, url: String?) =
    imageView.load(url) {
        crossfade(true)
        listener(onError = { _, errorResult ->
            (imageView.context as Activity).let { context ->
                dialog(
                    context, IOFailure(
                        context.getString(R.string.title_no_internet),
                        context.getString(R.string.message_no_internet)
                    )
                ).show()
                Log.e(TAG, "failed to load image $errorResult")
            }
        })
    }

@BindingAdapter("updateStatus")
fun setStatus(shimmerFrameLayout: ShimmerFrameLayout, status: WallpaperApiStatus?) {
    if (status is WallpaperApiStatus.Loading || status is WallpaperApiStatus.Failed) {
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmer()
    } else {
        shimmerFrameLayout.visibility = View.GONE
        Log.i(TAG, "shimmer has stopped")
    }
}

@BindingAdapter("drawableProfilePict")
fun setDrawableProfilePict(imageView: ImageView, src: Int) = imageView.setImageBitmap(
    decodeSampledBitmapFromResource(imageView.context.resources, src, 100, 100)
)

@BindingAdapter("drawableIcon")
fun setDrawableIcon(imageView: ImageView, src: Int) = imageView.setImageResource(src)