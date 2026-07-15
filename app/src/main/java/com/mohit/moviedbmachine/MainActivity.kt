package com.mohit.moviedbmachine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mohit.moviedbmachine.navigation.NavigationController
import com.mohit.moviedbmachine.ui.theme.MovieDBMachineTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieDBMachineTheme {
                NavigationController()
            }
        }
    }
}
