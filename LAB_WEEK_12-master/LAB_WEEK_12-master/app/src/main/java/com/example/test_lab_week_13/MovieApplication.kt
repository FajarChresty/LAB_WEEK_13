package com.example.test_lab_week_13

import android.app.Application
import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.database.MovieDatabase
import com.example.test_lab_week_13.repository.MovieRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MovieApplication : Application() {

    lateinit var movieRepository: MovieRepository

    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val movieService = retrofit.create(MovieService::class.java)
        val movieDatabase = MovieDatabase.getInstance(applicationContext)

        // SEBELUMNYA: MovieRepository(movieService, movieDatabase)
        // SEKARANG: Tambahkan context agar worker bisa jalan
        movieRepository = MovieRepository(
            movieService = movieService,
            movieDatabase = movieDatabase,
            context = this
        )
    }
}
