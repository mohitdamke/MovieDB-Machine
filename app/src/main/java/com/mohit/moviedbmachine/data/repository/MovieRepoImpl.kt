package com.mohit.moviedbmachine.data.repository

import com.mohit.moviedbmachine.data.mapper.toMovie
import com.mohit.moviedbmachine.data.mapper.toMovieDetail
import com.mohit.moviedbmachine.data.remote.ApiService
import com.mohit.moviedbmachine.domain.model.Movie
import com.mohit.moviedbmachine.domain.model.MovieDetail
import com.mohit.moviedbmachine.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepoImpl @Inject constructor(
    private val apiService: ApiService
) : MovieRepository {
    override suspend fun getPopularMovies(
        page: Int
    ): List<Movie> {
        return apiService
            .getPopularMovies(page = page)
            .results
            .map { it.toMovie() }

    }

    override suspend fun searchMovie(
        query: String,
        page: Int
    ): List<Movie> {
        return apiService
            .searchMovies(query = query, page = page)
            .results
            .map { it.toMovie() }
    }

    override suspend fun getMovieDetail(movieId: Int): MovieDetail {

        return apiService.getMovieDetail(movieId = movieId)
            .toMovieDetail()

    }
}