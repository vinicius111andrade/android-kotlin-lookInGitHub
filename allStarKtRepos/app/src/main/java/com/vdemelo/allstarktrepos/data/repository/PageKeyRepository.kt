package com.vdemelo.allstarktrepos.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vdemelo.allstarktrepos.data.api.GithubApi

/**
 * Created by Vinicius Andrade on 10/23/2021.
 */
class PageKeyRepository(private val githubApi: GithubApi) {
    fun reposOfGithub(language: String, pageSize: Int) = Pager(
        PagingConfig(pageSize)
    ) {
        PageKeyedGithubRepoPagingSource(
            githubApi = githubApi,
            language = language
        )
    }.flow
}