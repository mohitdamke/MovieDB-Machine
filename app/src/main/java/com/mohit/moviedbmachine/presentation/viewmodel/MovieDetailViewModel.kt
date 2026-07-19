package com.mohit.moviedbmachine.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohit.moviedbmachine.domain.model.MovieDetail
import com.mohit.moviedbmachine.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

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

    private var currentMovieId: Int? = null

    private var detailJob: Job? = null

    fun getMovieDetail(movieId: Int) {

        if (movieId <= 0) {
            _error.value = "Invalid movie id."
            return
        }

        if (currentMovieId == movieId &&
            _movieDetail.value != null
        ) {
            return
        }

        detailJob?.cancel()

        currentMovieId = movieId

        detailJob = viewModelScope.launch {

            _isLoading.value = true
            _error.value = null

            try {

                _movieDetail.value =
                    repository.getMovieDetail(movieId)

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                _error.value =
                    e.message ?: "Something went wrong."

            } finally {

                _isLoading.value = false

            }
        }
    }

    fun retry() {

        currentMovieId?.let {
            getMovieDetail(it)
        }

    }
}