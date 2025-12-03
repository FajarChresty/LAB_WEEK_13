package com.example.test_lab_week_13

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.test_lab_week_13.databinding.ActivityMainBinding
import com.example.test_lab_week_13.model.Movie
import com.example.test_lab_week_13.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import com.example.test_lab_week_13.BR

class MainActivity : AppCompatActivity() {

    private val movieAdapter by lazy {
        MovieAdapter(object : MovieAdapter.MovieClickListener {
            override fun onMovieClick(movie: Movie) {
                openMovieDetails(movie)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_main)

        binding.movieList.adapter = movieAdapter

        val movieRepository = (application as MovieApplication).movieRepository

        val movieViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return MovieViewModel(movieRepository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        )[MovieViewModel::class.java]

        binding.setVariable(BR.viewModel, movieViewModel)
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            movieViewModel.error.collect { error ->
                if (error.isNotEmpty()) {
                    Snackbar.make(binding.movieList, error, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun openMovieDetails(movie: Movie) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_TITLE, movie.title)
            putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate)
            putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview)
            putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath)
        }
        startActivity(intent)
    }
}
