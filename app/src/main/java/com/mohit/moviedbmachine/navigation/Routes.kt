package com.mohit.moviedbmachine.navigation

sealed class Routes(val routes : String) {

    data object MovieScreen : Routes("MovieScreen")
    data object MovieDetailScreen : Routes("MovieDetailScreen/{movieId}")
}