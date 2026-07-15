package com.mohit.moviedbmachine.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohit.moviedbmachine.presentation.screen.MovieDetailScreen
import com.mohit.moviedbmachine.presentation.screen.MovieScreen

@Composable
fun NavigationController(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = Routes.MovieScreen.routes
    ) {
        composable(route = Routes.MovieScreen.routes){
            MovieScreen(navController = navController)
        }

        composable(route = Routes.MovieDetailScreen.routes){
            val movieId = it.arguments
                ?.getString("movieId")
                ?.toInt() ?: 0
            MovieDetailScreen(movieId = movieId)
        }
    }


}