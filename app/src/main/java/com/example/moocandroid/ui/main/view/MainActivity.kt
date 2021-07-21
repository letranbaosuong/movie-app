package com.example.moocandroid.ui.main.view

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moocandroid.R
import com.example.moocandroid.data.api.ApiHelper
import com.example.moocandroid.data.api.RetrofitBuilder
import com.example.moocandroid.data.model.MovieModel.Result
import com.example.moocandroid.databinding.ActivityMainBinding
import com.example.moocandroid.ui.base.ViewModelFactory
import com.example.moocandroid.ui.main.adapter.MostPopularAdapter
import com.example.moocandroid.ui.main.adapter.PlayingNowAdapter
import com.example.moocandroid.ui.main.viewmodel.MainViewModel
import com.example.moocandroid.utils.Constants
import com.example.moocandroid.utils.Status.*


class MainActivity : AppCompatActivity(), MostPopularAdapter.OnMostPopularListener,
    PlayingNowAdapter.OnPlayingNowListener {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mAdapterPlayingNow: PlayingNowAdapter
    private lateinit var mAdapterMostPopular: MostPopularAdapter
    private lateinit var mBinding: ActivityMainBinding

    private var mPlayingNowMovies = ArrayList<Result>()
    private var isPlayingNowLoading: Boolean = true
    private var mPagePlayingNow: Int = 1

    private var mMostPopularMovies = ArrayList<Result>()
    private var isMostPopularLoading: Boolean = true
    private var mPageMostPopular: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        supportActionBar?.apply {
//            val colorDrawable = ColorDrawable(Color.parseColor("#d92616"))
//            setBackgroundDrawable(colorDrawable)
            // show custom title in action bar
            customView = actionBarCustomTitle()
            displayOptions = DISPLAY_SHOW_CUSTOM

            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
            // setLogo(R.drawable.ic_camera)
        }

        setupViewModel()
        setupUI()
        setupObservers()
        initScrollListener()
    }

    private fun actionBarCustomTitle(): TextView {
        return TextView(this).apply {
            "Movie Box".also { text = it }

            val params = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            // center align the text view/ action bar title
            params.gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = params

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setTextAppearance(
                    android.R.style.TextAppearance_Material_Widget_ActionBar_Title
                )
            } else {
                // define your own text style
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
                setTypeface(null, Typeface.BOLD)
            }
        }
    }

    private fun setupViewModel() {
        mViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        /// Playing now
        mBinding.recyclerViewPlayingNow.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mAdapterPlayingNow = PlayingNowAdapter(arrayListOf(), this)
        mBinding.recyclerViewPlayingNow.addItemDecoration(
            DividerItemDecoration(
                mBinding.recyclerViewPlayingNow.context,
                (mBinding.recyclerViewPlayingNow.layoutManager as LinearLayoutManager).orientation
            )
        )
        mBinding.recyclerViewPlayingNow.adapter = mAdapterPlayingNow

        /// Most popular
        mBinding.recyclerViewMostPopular.layoutManager = LinearLayoutManager(this)
        mAdapterMostPopular = MostPopularAdapter(arrayListOf(), this)
        mBinding.recyclerViewMostPopular.addItemDecoration(
            DividerItemDecoration(
                mBinding.recyclerViewMostPopular.context,
                (mBinding.recyclerViewMostPopular.layoutManager as LinearLayoutManager).orientation
            )
        )
        mBinding.recyclerViewMostPopular.adapter = mAdapterMostPopular
    }

    private fun retrievePlayingNow(results: ArrayList<Result>) {
        results.forEach {
            if (it.poster_path != null && it.backdrop_path != null) {
                mPlayingNowMovies.add(it)
            }
        }
        mAdapterPlayingNow.apply {
            addImageMovie(mPlayingNowMovies)
            notifyDataSetChanged()
            isPlayingNowLoading = true
        }
    }

    private fun retrieveMostPopular(results: ArrayList<Result>) {
        results.forEach {
            if (it.poster_path != null && it.backdrop_path != null) {
                mMostPopularMovies.add(it)
            }
        }
        mAdapterMostPopular.apply {
            addMovieItem(mMostPopularMovies)
            notifyDataSetChanged()
            isMostPopularLoading = true
        }
    }

    private fun setupObservers() {
        /// now_playing
        mViewModel.getMovies(type = "now_playing", language = Constants.vi, page = 1)
            .observe(this, {
                it?.let { resource ->
                    when (resource.status) {
                        SUCCESS -> {
                            mBinding.progressBarPlayingNow.visibility = View.GONE
                            mBinding.recyclerViewPlayingNow.visibility = View.VISIBLE
                            resource.data?.let { movieModel ->
                                movieModel.results?.let { it1 -> retrievePlayingNow(it1) }
                            }
                        }
                        ERROR -> {
                            mBinding.recyclerViewPlayingNow.visibility = View.VISIBLE
                            mBinding.progressBarPlayingNow.visibility = View.GONE
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        LOADING -> {
                            mBinding.progressBarPlayingNow.visibility = View.VISIBLE
                            mBinding.recyclerViewPlayingNow.visibility = View.GONE
                        }
                    }
                }
            })

        /// popular
        mViewModel.getMovies(type = "popular", language = Constants.vi, page = mPageMostPopular)
            .observe(this, {
                it?.let { resource ->
                    when (resource.status) {
                        SUCCESS -> {
                            mBinding.progressBarMostPopular.visibility = View.GONE
                            mBinding.recyclerViewMostPopular.visibility = View.VISIBLE
                            resource.data?.let { movieModel ->
                                movieModel.results?.let { it1 -> retrieveMostPopular(it1) }
                            }
                        }
                        ERROR -> {
                            mBinding.recyclerViewMostPopular.visibility = View.VISIBLE
                            mBinding.progressBarMostPopular.visibility = View.GONE
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        LOADING -> {
                            mBinding.progressBarMostPopular.visibility = View.VISIBLE
                            mBinding.recyclerViewMostPopular.visibility = View.GONE
                        }
                    }
                }
            })
    }

    override fun onMostPopularClick(position: Int) {
        mMostPopularMovies[position].id?.let { navigatorPushIdMovie(it) }
    }

    override fun onPlayingNowClick(position: Int) {
        mPlayingNowMovies[position].id?.let { navigatorPushIdMovie(it) }
    }

    private fun navigatorPushIdMovie(idMovie: Int) {
        Log.d("AAA", "onMostPopularClick id: $idMovie")

        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("idMovie", idMovie)
        startActivity(intent)
    }

    private fun initScrollListener() {
        /// Most popular
        mBinding.recyclerViewMostPopular.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (isMostPopularLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mMostPopularMovies.size - 1) {
                        //bottom of list!
                        loadMoreMostPopular(++mPageMostPopular)
                        isMostPopularLoading = false
                    }
                }
            }
        })

        /// Playing now
        mBinding.recyclerViewPlayingNow.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (isPlayingNowLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mPlayingNowMovies.size - 1) {
                        //bottom of list!
                        loadMorePlayingNow(++mPagePlayingNow)
                        isPlayingNowLoading = false
                    }
                }
            }
        })
    }

    private fun loadMoreMostPopular(page: Int) {
        /// popular
        mViewModel.getMovies(type = "popular", language = Constants.vi, page = page)
            .observe(this, {
                it?.let { resource ->
                    when (resource.status) {
                        SUCCESS -> {
                            mBinding.progressBarMostPopular.visibility = View.GONE
                            resource.data?.let { movieModel ->
                                movieModel.results?.let { it1 -> retrieveMostPopular(it1) }
                            }
                        }
                        ERROR -> {
                            mBinding.recyclerViewMostPopular.visibility = View.VISIBLE
                            mBinding.progressBarMostPopular.visibility = View.GONE
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        LOADING -> {
                            mBinding.progressBarMostPopular.visibility = View.VISIBLE
                        }
                    }
                }
            })
    }

    private fun loadMorePlayingNow(page: Int) {
        /// now_playing
        mViewModel.getMovies(type = "now_playing", language = Constants.vi, page = page)
            .observe(this, {
                it?.let { resource ->
                    when (resource.status) {
                        SUCCESS -> {
                            mBinding.progressBarPlayingNow.visibility = View.GONE
                            resource.data?.let { movieModel ->
                                movieModel.results?.let { it1 -> retrievePlayingNow(it1) }
                            }
                        }
                        ERROR -> {
                            mBinding.recyclerViewPlayingNow.visibility = View.VISIBLE
                            mBinding.progressBarPlayingNow.visibility = View.GONE
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        LOADING -> {
                            mBinding.progressBarPlayingNow.visibility = View.VISIBLE
                        }
                    }
                }
            })
    }
}