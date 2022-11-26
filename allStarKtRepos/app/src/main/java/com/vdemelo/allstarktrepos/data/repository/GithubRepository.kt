package com.vdemelo.allstarktrepos.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vdemelo.allstarktrepos.data.api.GithubApi
import com.vdemelo.allstarktrepos.data.model.GithubRepo
import com.vdemelo.allstarktrepos.data.paging.GithubPagingSource
import com.vdemelo.allstarktrepos.utils.Constants.PAGING_PAGE_SIZE
import kotlinx.coroutines.flow.Flow

class GithubRepository(
    private val apiService: GithubApi
) {

    fun getSearchResultStream(query: String): Flow<PagingData<GithubRepo>> = Pager(
            config = PagingConfig(
                pageSize = PAGING_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GithubPagingSource(apiService, query)
            }
        ).flow


}
