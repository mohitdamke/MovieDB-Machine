package com.mohit.moviedbmachine.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohit.moviedbmachine.navigation.Routes
import com.mohit.moviedbmachine.presentation.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun MovieScreen(navController: NavController) {
    val movieViewModel: MovieViewModel = hiltViewModel()
    val movie by movieViewModel.movie.collectAsState()
    val isLoading by movieViewModel.isLoading.collectAsState()
    val isLoadingMore by movieViewModel.isLoadingMore.collectAsState()
    val isError by movieViewModel.isError.collectAsState()
    val listState = rememberLazyListState()

    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
                .lastOrNull()
                ?.index
        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { index ->
                if (movie.isNotEmpty() &&
                    !isLoadingMore &&
                    index >= movie.lastIndex - 5
                ) {
                    movieViewModel.loadNextPage()
                }

            }

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {

            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.size(40.dp))
            }

            isError != null -> {
                Text(text = isError ?: "Unknown Error")
            }

            movie.isNotEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery, onValueChange = { searchQuery = it },
                        label = { Text("Search Here") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (searchQuery.isBlank()) {
                                    movieViewModel.clearSearch()
                                } else {
                                    movieViewModel.searchMovie(searchQuery)
                                }
                            }
                        )
                    )

                    LazyColumn(
                        state = listState
                    ) {
                        items(
                            items = movie,
                            key = { it.id!! }
                        ) { movie ->

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {

                                        navController.navigate(
                                            Routes.MovieDetailScreen.routes.replace(
                                                "{movieId}",
                                                "${movie.id}"
                                            ))

                                    }
                            ) {
                                movie.title?.let { Text(text = it) }
                                movie.releaseDate?.let { Text(text = it) }
                                Text(text = "⭐ ${movie.rating}")
                            }
                        }
                        if (isLoading && movie.isNotEmpty()) {
                            item {
                                CircularProgressIndicator()
                            }
                        }
                    }


                }

            }
        }


    }


}