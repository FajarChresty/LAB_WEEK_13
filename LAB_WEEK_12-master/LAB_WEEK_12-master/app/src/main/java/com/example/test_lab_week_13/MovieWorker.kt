package com.example.test_lab_week_13.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.database.MovieDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MovieWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val apiKey = "16cbedfc1d33d08ef90e77cc9daa3e1e"

    override suspend fun doWork(): Result {
        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

            val service = retrofit.create(MovieService::class.java)
            val database = MovieDatabase.getInstance(applicationContext)
            val movieDao = database.movieDao()

            // SEKARANG BOLEH MENGGUNAKAN SUSPEND FUNCTION
            val response = service.getPopularMovies(apiKey)

            movieDao.addMovies(response.results)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
