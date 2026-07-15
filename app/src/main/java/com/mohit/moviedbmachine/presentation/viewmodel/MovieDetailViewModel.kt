package com.mohit.moviedbmachine.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohit.moviedbmachine.domain.model.MovieDetail
import com.mohit.moviedbmachine.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail: StateFlow<MovieDetail?> = _movieDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getMovieDetail(movieId: Int) {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _error.value = null

                _movieDetail.value =
                    repository.getMovieDetail(movieId)

            } catch (e: Exception) {

                _error.value = e.message ?: "Unknown Error"

            } finally {

                _isLoading.value = false

            }
        }
    }
}