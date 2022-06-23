package com.cisnux.wallpaper_app.presentation.adapter

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cisnux.wallpaper_app.presentation.ui.DetailActivity
import com.cisnux.wallpaper_app.R
import com.cisnux.wallpaper_app.data.model.Wallpaper
import com.cisnux.wallpaper_app.databinding.GridWallpaperItemBinding
import com.cisnux.wallpaper_app.databinding.ListWallpaperItemBinding

class WallpapersAdapter(private val context: Context, private val isGrid: Boolean = false) :
    ListAdapter<Wallpaper, WallpapersAdapter.WallpapersViewHolder>(DiffCallback) {

    class WallpapersViewHolder(private val binding: ViewDataBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallpaper: Wallpaper) {
            if (binding is ListWallpaperItemBinding) {
                binding.listWallpaper = wallpaper
                binding.listViewHolder = this
                binding.listWallpaperImage.setColorFilter(
                    R.color.black_filter,
                    PorterDuff.Mode.OVERLAY
                )
            } else if (binding is GridWallpaperItemBinding) {
                binding.gridWallpaper = wallpaper
                binding.gridViewHolder = this
                binding.gridWallpaperImage.setColorFilter(
                    R.color.black_filter,
                    PorterDuff.Mode.OVERLAY
                )
            }
            binding.executePendingBindings()
        }

        fun navigateToDetail(wallpaper: Wallpaper) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(DetailActivity.WALLPAPER, wallpaper)
            }.also {
                context.startActivity(it)
            }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Wallpaper>() {
        // whether two objects represent the same Item?
        override fun areItemsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean =
            oldItem.id == newItem.id

        // whether two objects have the same data or content?
        override fun areContentsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean =
            oldItem.src == newItem.src
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WallpapersViewHolder =
        WallpapersViewHolder(
            if (isGrid) GridWallpaperItemBinding.inflate(LayoutInflater.from(parent.context)) else ListWallpaperItemBinding.inflate(
                LayoutInflater.from(parent.context)
            ), context
        )

    override fun onBindViewHolder(holder: WallpapersViewHolder, position: Int) =
        holder.bind(getItem(position))
}