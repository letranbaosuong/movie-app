package com.example.moocandroid.ui.main.adapter

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.moocandroid.data.model.MovieModel.Result
import com.example.moocandroid.databinding.PlayingNowItemBinding
import com.example.moocandroid.ui.main.adapter.PlayingNowAdapter.DataViewHolder
import com.example.moocandroid.utils.Constants

class PlayingNowAdapter(
    dataMovies: ArrayList<Result>,
    onPlayingNowListener: OnPlayingNowListener
) :
    RecyclerView.Adapter<DataViewHolder>() {

    private var mDataMovies: ArrayList<Result> = dataMovies
    private var mOnPlayingNowListener: OnPlayingNowListener = onPlayingNowListener

    class DataViewHolder(
        private val binding: PlayingNowItemBinding,
        onPlayingNowListener: OnPlayingNowListener
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var mOnPNowListener: OnPlayingNowListener = onPlayingNowListener

        fun bind(result: Result) {
            itemView.setOnClickListener(this)
            itemView.apply {
                val options: RequestOptions = RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_menu_report_image)
                    .error(R.drawable.ic_menu_report_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                Glide.with(binding.imageView.context)
                    .load("${Constants.urlImageSizeWidth500}${result.poster_path}")
                    .apply(options)
                    .into(binding.imageView)
            }
        }

        override fun onClick(v: View?) {
            mOnPNowListener.onPlayingNowClick(bindingAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            PlayingNowItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            mOnPlayingNowListener,
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        return holder.bind(mDataMovies[position])
    }

    override fun getItemCount(): Int = mDataMovies.size

    fun addImageMovie(results: List<Result>) {
        this.mDataMovies.apply {
            clear()
            addAll(results)
        }
    }

    interface OnPlayingNowListener {
        fun onPlayingNowClick(position: Int)
    }
}