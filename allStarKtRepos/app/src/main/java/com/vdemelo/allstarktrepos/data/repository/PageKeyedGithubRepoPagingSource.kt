package com.vdemelo.allstarktrepos.data.repository

import androidx.paging.PagingSource
import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Vinicius Andrade on 10/23/2021.
 */
class PageKeyedGithubRepoPagingSource(
    private val githubApi: GithubApi,
    private val language: String
) : PagingSource<String, GithubRepo>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, GithubRepo> {
        return try {
            val data = githubApi.getTop(
                page = 1,
                language = language,
//                after = if (params is Append) params.key else null,
//                before = if (params is Prepend) params.key else null,
//                limit = params.loadSize
            ).data

            Page(
                data = data.children.map { it.data },
                prevKey = data.before,
                nextKey = data.after
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, GithubRepo>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}