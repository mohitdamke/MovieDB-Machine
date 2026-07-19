package com.mohit.moviedbmachine.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohit.moviedbmachine.domain.model.Movie
import com.mohit.moviedbmachine.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movie = MutableStateFlow<List<Movie>>(emptyList())
    val movie: StateFlow<List<Movie>> = _movie

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private val _isError = MutableStateFlow<String?>(null)
    val isError: StateFlow<String?> = _isError

    private var currentPage = 1
    private var hasMorePages = true
    private var searchQuery = ""

    private var searchJob: Job? = null

    init {
        loadNextPage()
    }

    fun loadNextPage() {

        if (_isLoading.value || _isLoadingMore.value || !hasMorePages) {
            return
        }

        searchJob = viewModelScope.launch {

            _isError.value = null

            if (currentPage == 1) {
                _isLoading.value = true
            } else {
                _isLoadingMore.value = true
            }

            try {

                val newMovies =
                    if (searchQuery.isBlank()) {
                        repository.getPopularMovies(currentPage)
                    } else {
                        repository.searchMovie(
                            query = searchQuery,
                            page = currentPage
                        )
                    }

                if (newMovies.isEmpty()) {
                    hasMorePages = false
                    return@launch
                }

                _movie.value =
                    (_movie.value + newMovies)
                        .distinctBy { it.id }

                currentPage++

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {

                _isError.value =
                    e.message ?: "Something went wrong."

            } finally {

                _isLoading.value = false
                _isLoadingMore.value = false

            }
        }
    }

    fun searchMovie(query: String) {

        val trimmedQuery = query.trim()

        if (trimmedQuery == searchQuery) {
            return
        }

        searchJob?.cancel()

        searchQuery = trimmedQuery

        resetPagination()

        loadNextPage()
    }

    fun clearSearch() {

        if (searchQuery.isBlank()) {
            return
        }

        searchJob?.cancel()

        searchQuery = ""

        resetPagination()

        loadNextPage()
    }

    fun retry() {
        loadNextPage()
    }

    private fun resetPagination() {
        currentPage = 1
        hasMorePages = true
        _movie.value = emptyList()
        _isError.value = null
    }
}