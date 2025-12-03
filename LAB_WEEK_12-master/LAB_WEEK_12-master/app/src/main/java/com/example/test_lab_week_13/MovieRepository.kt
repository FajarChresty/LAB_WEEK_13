package com.example.test_lab_week_13.repository

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.database.MovieDatabase
import com.example.test_lab_week_13.model.Movie
import com.example.test_lab_week_13.worker.MovieWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase,
    private val context: Context
) {
    private val apiKey = "16cbedfc1d33d08ef90e77cc9daa3e1e"

    // logika lama tetap dipakai
    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            val movieDao = movieDatabase.movieDao()
            val savedMovies = movieDao.getMovies()

            if (savedMovies.isEmpty()) {
                val response = movieService.getPopularMovies(apiKey)
                val movies = response.results
                movieDao.addMovies(movies)
                emit(movies)
            } else {
                emit(savedMovies)
            }
        }.flowOn(Dispatchers.IO)
    }

    // FITUR TAMBAHAN SESUAI MODUL
    fun refreshMoviesWithWorker() {
        val request = OneTimeWorkRequestBuilder<MovieWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}
