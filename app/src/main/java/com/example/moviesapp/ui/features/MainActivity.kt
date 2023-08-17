package com.example.moviesapp.ui.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviesapp.ui.features.details.MovieDetailsScreen
import com.example.moviesapp.ui.features.details.MovieDetailsViewModel
import com.example.moviesapp.ui.features.list.MovieListScreen
import com.example.moviesapp.ui.features.list.MovieListViewModel
import com.example.moviesapp.ui.theme.MoviesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.Home.route) {
                    composable(Routes.Home.route) {
                        val movieListViewModel = hiltViewModel<MovieListViewModel>()
                        MovieListScreen(
                            movieListViewModel,
                            onMovieClicked = {
                                navController.navigate(
                                    "${Routes.Details.route}/${it.movieId}/${it.movieTitle}"
                                )
                            }
                        )
                    }
                    composable(
                        route = "${Routes.Details.route}/{$MOVIE_ID_KEY}/{$MOVIE_TITLE_KEY}",
                        arguments = listOf(
                            navArgument(MOVIE_ID_KEY) { type = NavType.IntType },
                            navArgument(MOVIE_TITLE_KEY) { type = NavType.StringType }
                        )
                    ) {
                        val movieDetailsViewModel = hiltViewModel<MovieDetailsViewModel>()
                        MovieDetailsScreen(
                            onBackPressed = { navController.popBackStack() },
                            viewModel = movieDetailsViewModel,
                            movieMetaData = MovieMetaData(
                                it.arguments?.getInt(MOVIE_ID_KEY),
                                it.arguments?.getString(MOVIE_TITLE_KEY)
                            )
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val MOVIE_ID_KEY = "movieId"
        const val MOVIE_TITLE_KEY = "movieTitle"
    }
}

data class MovieMetaData(val movieId: Int?, val movieTitle: String?)

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Details : Routes("details")
}