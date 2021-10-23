package com.vdemelo.allstarktrepos.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vdemelo.allstarktrepos.data.api.GithubApi

/**
 * Created by Vinicius Andrade on 10/23/2021.
 */
//class PageKeyedRemoteMediator(
//    val db: RedditDb,
//    val githubApi: GithubApi
//) {
//
//    @OptIn(ExperimentalPagingApi::class)
//    fun postsOfSubreddit(subReddit: String, pageSize: Int) = Pager(
//        config = PagingConfig(pageSize),
//        remoteMediator = PageKeyedRemoteMediator(db, githubApi, subReddit)
//    ) {
//        db.posts().postsBySubreddit(subReddit)
//    }.flow
//}