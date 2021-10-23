package com.vdemelo.allstarktrepos.data.api

import android.util.Log
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Vinicius Andrade on 10/23/2021.
 */
interface GithubApi {

    // https://api.github.com/search/repositories?q=language:kotlin&sort=stars&page=1

    @GET("search/repositories")
    suspend fun getTop(
        @Query("language") language: String = "kotlin",
        @Query("sort") sort: String = "stars",
        @Query("page") page: Int = 1
    ): ListingResponse

    class ListingResponse(val data: ListingData)

    class ListingData(
        val children: List<GithubChildrenResponse>,
        val after: String?,
        val before: String?
    )

    data class GithubChildrenResponse(val data: GithubRepo)

    companion object {
        private const val BASE_URL = "https://api.github.com/"
        fun create(): GithubApi {
            val logger = HttpLoggingInterceptor { Log.d("API", it) }
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrlOrNull()!!)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubApi::class.java)
        }
    }
}