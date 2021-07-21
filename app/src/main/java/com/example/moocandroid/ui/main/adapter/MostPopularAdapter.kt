package com.example.moocandroid.ui.main.adapter

import android.R
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.moocandroid.data.model.MovieModel.Result
import com.example.moocandroid.databinding.MostPupularItemBinding
import com.example.moocandroid.ui.main.adapter.MostPopularAdapter.DataViewHolder
import com.example.moocandroid.utils.Constants
import com.example.moocandroid.utils.Helpers
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalQueries.localDate
import kotlin.math.roundToInt


class MostPopularAdapter(
    dataMovies: ArrayList<Result>,
    onMostPopularListener: OnMostPopularListener
) :
    RecyclerView.Adapter<DataViewHolder>() {

    private var mDataMovies: ArrayList<Result> = dataMovies
    private var mOnMostPopularListener: OnMostPopularListener = onMostPopularListener

    class DataViewHolder(
        private val binding: MostPupularItemBinding,
        onMostPopularListener: OnMostPopularListener
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var mOnMPopularListener: OnMostPopularListener = onMostPopularListener

        @SuppressLint("SetTextI18n")
        fun bind(result: Result) {
            itemView.setOnClickListener(this)
            itemView.apply {
                val options: RequestOptions = RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_menu_report_image)
                    .error(R.drawable.ic_menu_report_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                Glide.with(binding.imageViewMostPopular.context)
                    .load("${Constants.urlImageSizeWidth500}${result.poster_path}")
                    .apply(options)
                    .into(binding.imageViewMostPopular)
                binding.textViewMovieTitle.text = result.title
                binding.textViewMovieDate.text = Helpers.dateFormatter(result.release_date)
                binding.textViewMovieTime.text = Helpers.dateFormatter(result.release_date)
                binding.textViewMovieDetail.text = result.overview
                binding.progressBarPercent.progress =
                    (result.vote_average?.let { (it * 10).roundToInt() } ?: 0)
                binding.textViewPercent.text =
                    "${(result.vote_average?.let { (it * 10).roundToInt() } ?: 0)}%"
            }
        }

        override fun onClick(v: View?) {
            mOnMPopularListener.onMostPopularClick(bindingAdapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            MostPupularItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            mOnMostPopularListener,
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        return holder.bind(mDataMovies[position])
    }

    override fun getItemCount(): Int = mDataMovies.size

    fun addMovieItem(results: ArrayList<Result>) {
        this.mDataMovies.apply {
            clear()
            addAll(results)
        }
    }

    interface OnMostPopularListener {
        fun onMostPopularClick(position: Int)
    }
}