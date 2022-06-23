package com.cisnux.wallpaper_app.presentation.utils


import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cisnux.wallpaper_app.R
import com.cisnux.wallpaper_app.data.model.Wallpaper
import com.cisnux.wallpaper_app.presentation.adapter.WallpapersAdapter
import com.cisnux.wallpaper_app.utils.Failure
import com.cisnux.wallpaper_app.utils.IOFailure
import com.cisnux.wallpaper_app.utils.TAG
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * submit listData to [WallpapersAdapter]
 * */
@BindingAdapter("listData", "status")
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<Wallpaper>?,
    status: WallpaperApiStatus
) {
    if (data != null && status is WallpaperApiStatus.Done) {
        recyclerView.visibility = View.VISIBLE
        val adapter = recyclerView.adapter as WallpapersAdapter
        adapter.submitList(data)
    } else if (status is WallpaperApiStatus.Failed) {
        if (status.failure is IOFailure)
            showNoInternetDialog(recyclerView.context as Activity, status.failure)
        Log.e(TAG, status.failure.message)
    }
}

@BindingAdapter("wallpaperItemSrc")
fun setWallpaperItemSrc(imageView: ImageView, url: String?) =
    Glide.with(imageView.context).load(url).into(imageView)

@BindingAdapter("wallpaperDetailSrc")
fun setWallpaperDetailSrc(imageView: ImageView, url: String?) =
    Glide.with(imageView.context).load(url).listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            (imageView.context as Activity).let { context ->
                showNoInternetDialog(
                    context, IOFailure(
                        context.getString(R.string.title_no_internet),
                        context.getString(R.string.message_no_internet)
                    )
                )
                e?.message?.let {
                    Log.e(TAG, it)
                }
            }
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean = false
    }).into(imageView)


@BindingAdapter("status")
fun setStatus(shimmerFrameLayout: ShimmerFrameLayout, status: WallpaperApiStatus) {
    shimmerFrameLayout.startShimmer()

    if (status is WallpaperApiStatus.Done) {
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
    }
}

@BindingAdapter("drawableSrc")
fun setDrawableSrc(imageView: ImageView, src: Int) = imageView.setImageResource(src)

// to handle no internet connection
fun showNoInternetDialog(context: Activity, failure: Failure): AlertDialog? =
    MaterialAlertDialogBuilder(context)
        .setTitle(failure.title)
        .setIcon(R.drawable.ic_no_internet)
        .setMessage(failure.message)
        .setCancelable(true)
        .setPositiveButton(context.resources.getString(R.string.retry_dialog)) { _, _ ->
            context.apply {
                finish()
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
        .show()