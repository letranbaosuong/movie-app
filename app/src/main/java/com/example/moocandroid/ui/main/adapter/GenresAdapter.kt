package com.example.moocandroid.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moocandroid.data.model.MovieDetailModel.Genre
import com.example.moocandroid.data.model.MovieModel
import com.example.moocandroid.databinding.GenreItemBinding
import com.example.moocandroid.ui.main.adapter.GenresAdapter.ViewHolder

class GenresAdapter(private val genres: ArrayList<Genre>) :
    RecyclerView.Adapter<ViewHolder>() {

    class ViewHolder(private val binding: GenreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Genre) {
            itemView.apply {
                binding.textViewGenre.text = genre.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GenreItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(genres[position])

    override fun getItemCount(): Int = genres.size

    fun addGenre(genres: List<Genre>) {
        this.genres.apply {
            clear()
            addAll(genres)
        }
    }
}