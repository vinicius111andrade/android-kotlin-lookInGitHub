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

            val nextKey: Int? =
                if (repos.isEmpty())
                    null
                else
                    position + (params.loadSize / NETWORK_PAGE_SIZE)


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

    override fun getRefreshKey(state: PagingState<Int, GithubRepo>): Int? {

        val anchorPosition: Int? = state.anchorPosition

        val currentPageObject:  PagingSource.LoadResult.Page<Int, GithubRepo>? =
            anchorPosition?.let {
            state.closestPageToPosition(it)
        }

        val prevKey: Int? = currentPageObject?.prevKey
        val nextKey: Int? = currentPageObject?.nextKey

        return when {
            prevKey != null -> prevKey + 1
            nextKey != null -> nextKey + 1
            else -> null
        }

    }
}