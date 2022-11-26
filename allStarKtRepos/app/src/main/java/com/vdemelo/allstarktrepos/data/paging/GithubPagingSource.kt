package com.vdemelo.allstarktrepos.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.utils.Constants.PAGING_PAGE_SIZE
import com.vdemelo.allstarktrepos.utils.Constants.PAGING_STARTING_PAGE_INDEX

private const val IN_QUALIFIER = "in:name,description"

class GithubPagingSource(
    private val apiService: GithubApi,
    private val query: String
) : PagingSource<Int, GithubRepo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubRepo> {
        val position = params.key ?: PAGING_STARTING_PAGE_INDEX
        val apiQuery = query + IN_QUALIFIER

        return try {

            val response = apiService.searchGithub(
                query = apiQuery,
                page = position,
                perPage = params.loadSize
            )

            val repos = response.items

            val nextKey: Int? =
                if (repos.isEmpty())
                    null
                else
                    position + (params.loadSize / PAGING_PAGE_SIZE)


            LoadResult.Page(
                data = repos,
                prevKey = if (position == PAGING_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        }

        catch (exception: Throwable) {
            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, GithubRepo>): Int? {

        val anchorPosition: Int? = state.anchorPosition

        val currentPageObject: LoadResult.Page<Int, GithubRepo>? =
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
