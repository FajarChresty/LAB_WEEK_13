package com.example.test_lab_week_13.repository

import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(private val movieService: MovieService) {

    private val apiKey = "16cbedfc1d33d08ef90e77cc9daa3e1e"

    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            val response = movieService.getPopularMovies(apiKey)

            emit(
                response.results
                    .sortedByDescending { it.popularity }
            )
        }.flowOn(Dispatchers.IO)
    }
}
