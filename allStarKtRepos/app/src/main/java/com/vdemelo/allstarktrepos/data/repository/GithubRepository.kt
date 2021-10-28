package com.vdemelo.allstarktrepos.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.data.paging.GithubPagingSource
import com.vdemelo.allstarktrepos.utils.Constants.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

/**
 * Created by Vinicius Andrade on 10/25/2021.
 */
class GithubRepository(
    private val apiService: GithubApi
) {

    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    fun getSearchResultStream(query: String): Flow<PagingData<GithubRepo>> {

        Timber.d("GithubRepository ---- New query: $query")

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { GithubPagingSource(apiService, query) }
        ).flow
    }

}