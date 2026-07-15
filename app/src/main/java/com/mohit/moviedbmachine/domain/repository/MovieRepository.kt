package com.mohit.moviedbmachine.domain.repository

import androidx.room.Query
import com.mohit.moviedbmachine.domain.model.Movie
import com.mohit.moviedbmachine.domain.model.MovieDetail

interface MovieRepository {

    suspend fun getPopularMovies(page: Int): List<Movie>

    suspend fun searchMovie(
        query: String, page: Int
    ): List<Movie>

    suspend fun getMovieDetail(
        movieId : Int
    ): MovieDetail

}