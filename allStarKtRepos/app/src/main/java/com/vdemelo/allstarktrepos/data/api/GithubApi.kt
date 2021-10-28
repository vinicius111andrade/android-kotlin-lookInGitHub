package com.vdemelo.allstarktrepos.data.api

import com.vdemelo.allstarktrepos.utils.Constants.NETWORK_PAGE_SIZE
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber

/**
 * Created by Vinicius Andrade on 10/23/2021.
 */

const val IN_QUALIFIER = "in:name,description"

interface GithubApi {

    // https://api.github.com/search/repositories?q=language:kotlin&sort=stars&page=1

    @GET("search/repositories")
    suspend fun searchGithub(
        @Query("q") query: String = "kotlin",
        @Query("sort") sort: String = "stars",
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = NETWORK_PAGE_SIZE
    ): SearchResponse

    companion object {
        private const val BASE_URL = "https://api.github.com/"
        fun create(): GithubApi {
            val logger = HttpLoggingInterceptor { Timber.d("API $it") }
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubApi::class.java)
        }
    }
}