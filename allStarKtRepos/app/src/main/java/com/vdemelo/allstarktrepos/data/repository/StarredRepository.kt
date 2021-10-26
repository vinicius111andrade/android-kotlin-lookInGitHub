package com.vdemelo.allstarktrepos.data.repository

import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.api.IN_QUALIFIER
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.data.model.SearchResult
import com.vdemelo.allstarktrepos.utils.Constants.GITHUB_STARTING_PAGE_INDEX
import com.vdemelo.allstarktrepos.utils.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * Created by Vinicius Andrade on 10/25/2021.
 */
class StarredRepository(
    private val apiService: GithubApi
) {

    private val inMemoryCache = mutableListOf<GithubRepo>()

    private val searchResults = MutableSharedFlow<SearchResult>(replay = 1)

    private var lastRequestedPage = GITHUB_STARTING_PAGE_INDEX

    private var isRequestInProgress = false


    suspend fun getSearchResultStream(query: String): Flow<SearchResult> {
        Timber.d("GithubRepository ---- New query: $query")
        lastRequestedPage = 1
        inMemoryCache.clear()
        requestAndSaveData(query)

        return searchResults
    }

    suspend fun requestMore(query: String) {
        if (isRequestInProgress) return
        val successful = requestAndSaveData(query)
        if (successful) {
            lastRequestedPage++
        }
    }

    suspend fun retry(query: String) {
        if (isRequestInProgress) return
        requestAndSaveData(query)
    }

    private suspend fun requestAndSaveData(query: String): Boolean {
        isRequestInProgress = true
        var successful = false

        val apiQuery = query + IN_QUALIFIER

        try {

            val response = apiService.searchGithub(
                query = apiQuery,
                page = lastRequestedPage,
                per_page = ITEMS_PER_PAGE
            )

            Timber.d("GithubRepository ---- response $response")

            val repos: List<GithubRepo?> = response.items
            inMemoryCache.addAll(repos.filterNotNull())

            val reposByName = reposByName(query)
            searchResults.emit(SearchResult.Success(reposByName))
            successful = true
        }

        catch (exception: IOException) {
            searchResults.emit(SearchResult.Error(exception))
        }

        catch (exception: HttpException) {
            searchResults.emit(SearchResult.Error(exception))
        }

        isRequestInProgress = false
        return successful
    }

    private fun reposByName(query: String): List<GithubRepo> {
        return inMemoryCache.filter {
            it.name.contains(query, true) || it.description.contains(query, true)
        }.sortedWith(compareByDescending<GithubRepo> { it.stargazersCount }.thenBy { it.name })
    }

}