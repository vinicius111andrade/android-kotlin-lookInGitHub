package com.vdemelo.allstarktrepos.data.model

import java.lang.Exception

/**
 * Created by Vinicius Andrade on 10/25/2021.
 */
sealed class SearchResult {
    data class Success(val data: List<GithubRepo>) : SearchResult()
    data class Error(val error: Exception) : SearchResult()
}