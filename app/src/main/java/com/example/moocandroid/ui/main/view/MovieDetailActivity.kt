package com.example.moocandroid.ui.main.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.moocandroid.R
import com.example.moocandroid.data.api.ApiHelper
import com.example.moocandroid.data.api.RetrofitBuilder
import com.example.moocandroid.data.model.MovieDetailModel
import com.example.moocandroid.databinding.ActivityMovieDetailBinding
import com.example.moocandroid.ui.base.ViewModelFactory
import com.example.moocandroid.ui.main.adapter.GenresAdapter
import com.example.moocandroid.ui.main.viewmodel.MainViewModel
import com.example.moocandroid.utils.Constants
import com.example.moocandroid.utils.Helpers
import com.example.moocandroid.utils.Status

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding: ActivityMovieDetailBinding
    private lateinit var mAdapterGenre: GenresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        mBinding = ActivityMovieDetailBinding.inflate(layoutInflater)
        this.supportActionBar!!.hide()
        setContentView(mBinding.root)
        val idMovie: Int = intent.getIntExtra("idMovie", -1)
        mBinding.imageButton.setOnClickListener {
            finish()
        }

        setupViewModel()
        setupUI()
        setupObservers(idMovie)
    }

    private fun setupViewModel() {
        mViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        /// id
        mBinding.recyclerViewMovieGenres.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mAdapterGenre = GenresAdapter(arrayListOf())
        mBinding.recyclerViewMovieGenres.addItemDecoration(
            DividerItemDecoration(
                mBinding.recyclerViewMovieGenres.context,
                (mBinding.recyclerViewMovieGenres.layoutManager as LinearLayoutManager).orientation
            )
        )
        mBinding.recyclerViewMovieGenres.adapter = mAdapterGenre
    }

    private fun setupObservers(idMovie: Int?) {
        /// id
        mViewModel.getMovieById(id = idMovie ?: -1, language = Constants.vi)
            .observe(this, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            mBinding.progressBarMovieDetail.visibility = View.GONE
                            mBinding.linearLayoutMovieDetail.visibility = View.VISIBLE
                            resource.data?.let { detailMovie -> retrieveMovieById(detailMovie) }
                        }
                        Status.ERROR -> {
                            mBinding.linearLayoutMovieDetail.visibility = View.VISIBLE
                            mBinding.progressBarMovieDetail.visibility = View.GONE
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {
                            mBinding.progressBarMovieDetail.visibility = View.VISIBLE
                            mBinding.linearLayoutMovieDetail.visibility = View.GONE
                        }
                    }
                }
            })
    }

    private fun retrieveMovieById(movie: MovieDetailModel) {
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(android.R.drawable.ic_menu_report_image)
            .error(android.R.drawable.ic_menu_report_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
        Glide.with(mBinding.imageViewMovieDetail.context)
            .load("${Constants.urlImageOriginal}${movie.backdrop_path}")
            .apply(options)
            .into(mBinding.imageViewMovieDetail)
        mBinding.textViewMovieTitle.text = movie.title
        mBinding.textViewMovieDateTime.text = Helpers.dateFormatter(movie.release_date)
        mBinding.textViewMovieDetail.text = movie.overview

        mAdapterGenre.apply {
            movie.genres?.let { addGenre(it) }
            notifyDataSetChanged()
        }
    }
}