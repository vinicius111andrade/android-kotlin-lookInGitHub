package com.vdemelo.allstarktrepos.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.api.IN_QUALIFIER
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.utils.Constants.GITHUB_STARTING_PAGE_INDEX
import com.vdemelo.allstarktrepos.utils.Constants.NETWORK_PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Vinicius Andrade on 10/26/2021.
 */
class GithubPagingSource(
    private val apiService: GithubApi,
    private val query: String
) : PagingSource<Int, GithubRepo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubRepo> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        val apiQuery = query + IN_QUALIFIER

        return try {

            val response = apiService.searchGithub(
                query = apiQuery,
                page = position,
                per_page = params.loadSize
            )

            val repos = response.items

            val nextKey =
                if (repos.isEmpty()) {
                    null
                } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / NETWORK_PAGE_SIZE)
                }

            LoadResult.Page(
                data = repos,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        }

        catch (exception: IOException) {
            LoadResult.Error(exception)
        }

        catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, GithubRepo>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}