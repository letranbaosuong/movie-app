package com.example.moocandroid.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.moocandroid.data.repository.MainRepository
import com.example.moocandroid.utils.Resource
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getMovies(type: String?, language: String?, page: Int?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = mainRepository.getMovies(
                        type = type,
                        language = language,
                        page = page,
                    )
                )
            )
        } catch (exception: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = exception.message ?: "Error Occurred! >>> getMovies"
                )
            )
        }
    }

    fun getMovieById(id: Int?, language: String?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = mainRepository.getMovieById(
                        id = id,
                        language = language,
                    )
                )
            )
        } catch (exception: Exception) {
            emit(
                Resource.error(
                    data = null,
                    message = exception.message ?: "Error Occurred! >>> getMovieById"
                )
            )
        }
    }
}