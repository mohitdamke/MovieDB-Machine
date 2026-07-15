package com.mohit.moviedbmachine.presentation.viewmodel

import android.util.Log
import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohit.moviedbmachine.domain.model.Movie
import com.mohit.moviedbmachine.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movie = MutableStateFlow<List<Movie>>(emptyList())
    val movie : StateFlow<List<Movie>> = _movie

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading : StateFlow<Boolean> = _isLoading

    private val _isLoadingMore = MutableStateFlow<Boolean>(false)
    val isLoadingMore : StateFlow<Boolean> = _isLoadingMore

    private val _isError = MutableStateFlow<String?>(null)
    val isError : StateFlow<String?> = _isError

    private var currentPage = 1
    private var hasMorePages = true
    private var searchQuery = ""

    init {
        loadNextPage()
    }

       fun loadNextPage(){

           if (_isLoading.value || _isLoadingMore.value || !hasMorePages) return

           viewModelScope.launch {
            _isLoadingMore.value = true

            try {
                if (currentPage == 1) {
                    _isLoading.value = true
                } else {
                    _isLoadingMore.value = true
                }
                val newMovies =
                if (searchQuery.isBlank()){
                 repository.getPopularMovies(page = currentPage)}
                else {
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
                Log.d("WaoooooTAG", "loadNextPage: WAOOO   @$currentPage  ${newMovies.size} ")

                currentPage++
            }catch (e : Exception){
                _isError.value = e.message.toString()
            } finally {
                _isLoadingMore.value = false
                _isLoading.value = false
            }

        }


    }

    fun searchMovie(query : String){
        if(searchQuery == query) return

        searchQuery = query
        currentPage = 1
        hasMorePages = true
        _movie.value = emptyList()
        loadNextPage()
    }
    
    fun clearSearch(){
        searchQuery = ""
        currentPage = 1
        hasMorePages = true
        _movie.value = emptyList()
        loadNextPage()

    }


}






